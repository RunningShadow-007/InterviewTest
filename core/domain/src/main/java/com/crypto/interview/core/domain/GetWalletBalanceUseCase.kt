package com.crypto.interview.core.domain


import com.crypto.interview.core.common.maths.BigDecimalUtils
import com.crypto.interview.core.data.repository.wallet.WalletDataRepository
import com.crypto.interview.core.domain.model.CurrencyItem
import com.crypto.interview.core.domain.model.WalletData
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 11:38<br>
 * Desc: <br>
 */
@Singleton
class GetWalletBalanceUseCase @Inject constructor(private val repository: WalletDataRepository) {

    fun getWalletData(): Flow<NetworkResponse<WalletData>> = flow {
        coroutineScope {
            val deferredBalances = async { repository.getWalletBalances() }
            val deferredCurrencies = async { repository.getCurrencies() }
            val deferredRates = async { repository.getExchangeRates() }

            val balances = deferredBalances.await()
            val currencies = deferredCurrencies.await()
            val rates = deferredRates.await()
            val result =
                if (balances is NetworkResponse.Success && currencies is NetworkResponse.Success && rates is NetworkResponse.Success) {
                    handleSuccessData(balances, currencies, rates)
                } else {
                    handleErrorMessage(balances, currencies, rates)
                }
            emit(result)
        }
    }

}

private fun handleErrorMessage(
    balances: NetworkResponse<List<WalletBalance>>,
    currencies: NetworkResponse<List<Currency>>,
    rates: NetworkResponse<List<ExchangeRate>>
): NetworkResponse.Error {
    // 收集所有错误信息
    val errors = listOf(balances, currencies, rates)
        .filterIsInstance<NetworkResponse.Error>()
        .map { it.error }
    return NetworkResponse.Error(
        ok = false,
        error = GetWalletBalancesException(errors as List<String>).message
            ?: "Unknown error"
    )
}

private fun handleSuccessData(
    balances: NetworkResponse.Success<List<WalletBalance>>,
    currencies: NetworkResponse.Success<List<Currency>>,
    rates: NetworkResponse.Success<List<ExchangeRate>>
): NetworkResponse.Success<WalletData> {
    var rates1 = rates
    val balanceMap = balances.data.associate { it.currency to it.amount }
    val currencyMap = currencies.data.associate { it.code to it }
    val ratesMap = rates1.data.associate { it.fromCurrency to it.rates[0].rate }

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
    return NetworkResponse.Success(
        ok = true,
        data = WalletData(
            totalUsdValue = totalUsdValue,
            walletBalances = walletCoinBalanceList
        )
    )
}

class GetWalletBalancesException(errors: List<String>) : Exception(
    "Multiple errors occurred:\n${errors.joinToString("\n") { "• $it" }}"
)
