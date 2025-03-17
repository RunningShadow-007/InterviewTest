package com.crypto.interview.core.data.repository.defi

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.network.datasource.defi.DeFiDataSource
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
 */

@Singleton
@OptIn(InternalSerializationApi::class)
class DeFiDataRepositoryImpl constructor(
//    private val deFiDataSource: DeFiDataSource,
) :
    DeFiDataRepository {

    override fun getDeFiData(): Flow<NetworkResponse<Any>> {
        //mock getDeFiData from DataBase or Network or DataStore
        return flow {
            emit(NetworkResponse.Success(data = Any(), ok = true))
        }
    }
}