package com.crypto.interview.core.domain


import android.util.Log
import com.crypto.interview.core.data.di.DataModule
import com.crypto.interview.core.data.repository.wallet.WalletDataRepository
import com.crypto.interview.core.data.repository.wallet.WalletDataRepositoryImpl
import com.crypto.interview.core.domain.model.CurrencyItem
import com.crypto.interview.core.domain.model.WalletData
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 11:38<br>
 * Desc: <br>
 */
class GetWalletBalanceUseCase private constructor() {
    companion object {
        @Volatile
        private var instance: GetWalletBalanceUseCase? = null
        fun getInstance(): GetWalletBalanceUseCase {
            return instance ?: synchronized(this) {
                instance ?: GetWalletBalanceUseCase().also { instance = it }
            }
        }
    }

    private val repository = DataModule.provideWalletDataRepository()

     fun getWalletData(): Flow<NetworkResponse<WalletData>> = flow{
        coroutineScope {
            val deferredBalances = async { repository.getWalletBalances() }
            val deferredCurrencies = async { repository.getCurrencies() }
            val deferredRates = async { repository.getExchangeRates() }

            // 等待所有结果
            val balances = deferredBalances.await()
            val currencies = deferredCurrencies.await()
            val rates = deferredRates.await()
            Log.d("UIDebug", "balances: $balances,\ncurrencies: $currencies,\nrates: $rates")
            val result =
                if (balances is NetworkResponse.Success && currencies is NetworkResponse.Success && rates is NetworkResponse.Success) {
                    val walletBalances = balances.data
                    val currencyList = currencies.data
                    val exchangeRates = rates.data
                    // 计算所需的UI数据
                    val items = currencyList?.map { currency ->
                        val balance =
                            walletBalances?.find { it.currency == currency.code }?.amount ?: 0.0
                        val rate =
                            exchangeRates?.find { it.fromCurrency == currency.code && it.toCurrency == "USD" }?.rate
                                ?: 0.0
                        val usdValue = balance * rate
                        CurrencyItem(
                            currency = currency,
                            balance = balance,
                            usdValue = usdValue
                        )
                    }
                    NetworkResponse.Success(
                        ok = true,
                        data = WalletData(
                            totalUsdValue = items?.sumOf { it.usdValue } ?: 0.0,
                            walletBalances = items ?: emptyList()
                        )
                    )
                } else {
                    // 收集所有错误信息
                    val errors = listOf(balances, currencies, rates)
                        .filterIsInstance<NetworkResponse.Error>()
                        .map { it.error }
                    NetworkResponse.Error(
                        ok = false,
                        error = GetWalletBalancesException(errors as List<String>).message
                            ?: "Unknown error"
                    )
                }
            emit(result)
        }
    }

}

class GetWalletBalancesException(errors: List<String>) : Exception(
    "Multiple errors occurred:\n${errors.joinToString("\n") { "• $it" }}"
)
