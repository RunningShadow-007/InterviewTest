
package com.crypto.interview.core.data.repository.defi

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.network.datasource.defi.DeFiDataSource
import com.crypto.interview.core.network.datasource.defi.DeFiDataSourceImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright:Members
 * Author: liyang <br>
 * Date:2025/3/16 12:40<br>
 * Desc:
 */
//@Singleton
internal class DeFiDataRepositoryImpl private constructor(
    private val deFiDataSource: DeFiDataSource= DeFiDataSourceImpl.getInstance(),
) :
    DeFiDataRepository {

    override suspend fun getDeFiData(): NetworkResponse<Any> {
        return  deFiDataSource.getDeFiData()
    }

    companion object {
        @Volatile
        private var instance: DeFiDataRepositoryImpl? = null

        fun getInstance(): DeFiDataRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: DeFiDataRepositoryImpl().also { instance = it }
            }
        }
    }
}