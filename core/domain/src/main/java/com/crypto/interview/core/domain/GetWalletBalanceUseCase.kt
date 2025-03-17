package com.crypto.interview.core.domain


import android.util.Log
import com.crypto.interview.core.common.maths.BigDecimalUtils
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

    fun getWalletData(): Flow<NetworkResponse<WalletData>> = flow {
        coroutineScope {
            val deferredBalances = async { repository.getWalletBalances() }
            val deferredCurrencies = async { repository.getCurrencies() }
            val deferredRates = async { repository.getExchangeRates() }

            val balances = deferredBalances.await()
            val currencies = deferredCurrencies.await()
            val rates = deferredRates.await()
            Log.d("UIDebug", "balances: $balances,\ncurrencies: $currencies,\nrates: $rates")
            val result =
                if (balances is NetworkResponse.Success && currencies is NetworkResponse.Success && rates is NetworkResponse.Success) {
                    val balanceMap = balances.data.associate { it.currency to it.amount }
                    val currencyMap = currencies.data.associate { it.code to it }
                    val ratesMap = rates.data.associate { it.fromCurrency to it.rates[0].rate }

                    val walletCoinBalanceList = balanceMap.map {
                        val amount = it.value
                        val code = it.key
                        val rates = ratesMap[code] ?: "0.0"
                        val currency = currencyMap[code] ?: Currency()
                        val usdValue = BigDecimalUtils.calculateUsdValue(
                            amount,
                            rates,
                            scale = currency.displayDecimal
                        )
                        CurrencyItem(
                            currency = currency,
                            amount = amount,
                            usdValue = usdValue
                        )
                    }
                    val usdList = walletCoinBalanceList.map { it.usdValue }
                    val totalUsdValue = BigDecimalUtils.calculateTotalBalance(usdList)
                    val data = WalletData(
                        totalUsdValue = totalUsdValue,
                        walletBalances = walletCoinBalanceList
                    )
                    Log.e("UIDebug", "data: $data")
                    NetworkResponse.Success(
                        ok = true,
                        data = WalletData(
                            totalUsdValue = totalUsdValue,
                            walletBalances = walletCoinBalanceList
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
