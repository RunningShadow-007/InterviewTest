package com.crypto.interview.core.network.datasource.wallet

import androidx.annotation.OptIn
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:10<br>
 * Desc: <br>
 */
@OptIn(InternalSerializationApi::class)
@Singleton
internal class WalletDataSourceImpl @Inject constructor(private val walletApi: WalletApi) :
    WalletDataSource {

    @kotlin.OptIn(InternalSerializationApi::class)
    override suspend fun getWalletBalances(): NetworkResponse<List<WalletBalance>> =
        walletApi.getWalletBalances()

    @kotlin.OptIn(InternalSerializationApi::class)
    override suspend fun getExchangeRates(): NetworkResponse<List<ExchangeRate>> {
        return walletApi.getExchangeRates()
    }

    @kotlin.OptIn(InternalSerializationApi::class)
    override suspend fun getCurrencies(): NetworkResponse<List<Currency>> {
        return walletApi.getCurrencies()
    }
}