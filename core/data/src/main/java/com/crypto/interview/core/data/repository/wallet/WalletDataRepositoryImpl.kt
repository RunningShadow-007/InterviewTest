package com.crypto.interview.core.data.repository.wallet


import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import com.crypto.interview.core.network.datasource.wallet.WalletDataSource
import kotlinx.serialization.InternalSerializationApi

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright:Members
 * Author: liyang <br>
 * Date:2025/3/16 12:40<br>
 * Desc:
 *
 * Get wallet data from remote or local if business needed!
 * We can also cache data to local database, datastore or any appropriate place!
 *
 */
@Singleton
 class WalletDataRepositoryImpl @Inject constructor(
    private val remoteDataSource: WalletDataSource,
) :
    WalletDataRepository {
    /**
     * local dataSource ,
     * just a showcase to demonstrate there are different dataSource
     */
//    private val localDataSource= CryptoDataBase.getInstance()

//    companion object {
//        @Volatile
//        private var instance: WalletDataRepositoryImpl? = null
//        fun getInstance(): WalletDataRepositoryImpl {
//            return instance ?: synchronized(this) {
//                instance ?: WalletDataRepositoryImpl().also { instance = it }
//            }
//        }
//    }

    override suspend fun getWalletBalances(): NetworkResponse<List<WalletBalance>> {
        return remoteDataSource.getWalletBalances()
    }

    override suspend fun getExchangeRates(): NetworkResponse<List<ExchangeRate>> {
        return remoteDataSource.getExchangeRates()
    }

    override suspend fun getCurrencies(): NetworkResponse<List<Currency>> {
        return remoteDataSource.getCurrencies()
    }

}