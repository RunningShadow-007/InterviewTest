package com.crypto.interview.core.network.datasource.wallet

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import com.crypto.interview.core.network.di.NetworkModule
//import com.crypto.interview.core.network.di.NetworkModule.Companion.provideService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.getValue

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:10<br>
 * Desc: <br>
 */

 class WalletDataSourceImpl private constructor() :
    WalletDataSource {

    companion object {
        @Volatile
        private var instance: WalletDataSourceImpl? = null
        fun getInstance(): WalletDataSourceImpl {
            return instance?: synchronized(this) {
                instance?: WalletDataSourceImpl().also { instance = it }
            }
        }
    }
    private val walletApi by lazy {
//        retrofit.provideService(WalletApi::class.java)
        NetworkModule.getInstance().provideService(WalletApi::class.java)
    }

    override suspend fun getWalletBalances(): NetworkResponse<List<WalletBalance>> =
        walletApi.getWalletBalances()

    override suspend fun getExchangeRates(): NetworkResponse<List<ExchangeRate>> {
        return walletApi.getExchangeRates()
    }

    override suspend fun getCurrencies(): NetworkResponse<List<Currency>> {
        return walletApi.getCurrencies()
    }
}