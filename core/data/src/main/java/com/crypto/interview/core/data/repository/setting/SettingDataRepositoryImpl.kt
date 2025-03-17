package com.crypto.interview.core.data.repository.setting

import com.crypto.interview.core.database.CryptoDataBase
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.network.datasource.settings.SettingDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright:Members
 * Author: liyang <br>
 * Date:2025/3/16 12:40<br>
 * Desc:
 * It's just a code example to illustrate the actual responsibilities of a Repository,
 * which is to retrieve data from various data sources,
 * such as remote data sources or local data sources.
 */
@Singleton
@OptIn(InternalSerializationApi::class)
class SettingDataRepositoryImpl constructor(
//    private val remoteDataSource: SettingDataSource,
) :
    SettingDataRepository {

    private val localDataSource: CryptoDataBase = CryptoDataBase.getInstance()
//    private val remoteDataSource: SettingDataSource,
    /**
     * It's just a code example to illustrate the actual responsibilities of a Repository,
     */
    override fun getSetting(): Flow<Any> {
        return flow<Any> {
            if (isNetworkAvailable()) {
                //Fake code

                //1. remoteDataSource.getSetting()
                //2. save to local
                //3.localDataSource.saveSettingData()
                emit(NetworkResponse.Success(data = Any(), ok = true))
            } else {
                emit(localDataSource.getSettingData())
            }
        }

    }

    /**
     * mock network status
     */
    fun isNetworkAvailable(): Boolean {
        return true
    }

}