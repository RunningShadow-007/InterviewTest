package com.crypto.interview.core.network.datasource.settings

import com.crypto.interview.core.model.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:10<br>
 * Desc: <br>
 */
@Singleton
@OptIn(InternalSerializationApi::class)
internal class SettingDataSourceImpl @Inject constructor() :
    SettingDataSource {

    override fun getSetting(): Flow<Any> {
        return flow {
            emit(Any())
        }
    }
}