package com.crypto.interview.core.data.repository.setting

import com.crypto.interview.core.database.CryptoDataBase
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.network.datasource.settings.SettingDataSource
import com.crypto.interview.core.network.datasource.settings.SettingDataSourceImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
//@Singleton
 internal class SettingDataRepositoryImpl private constructor(
    private val remoteDataSource: SettingDataSource=SettingDataSourceImpl.getInstance(),
) :
    SettingDataRepository {

    private val localDataSource: CryptoDataBase = CryptoDataBase.getInstance()

    companion object {
        @Volatile
        private var instance: SettingDataRepositoryImpl? = null
        fun getInstance(): SettingDataRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: SettingDataRepositoryImpl().also { instance = it }
            }
        }
    }

    /**
     * It's just a code example to illustrate the actual responsibilities of a Repository,
     */
    override suspend fun getSetting(): NetworkResponse<Any> {
        if (isNetworkAvailable()) {
            return remoteDataSource.getSetting()
        }else{
            return localDataSource.getSettingData()
        }
    }

    /**
     * mock network status
     */
    fun isNetworkAvailable(): Boolean {
        return true
    }

}