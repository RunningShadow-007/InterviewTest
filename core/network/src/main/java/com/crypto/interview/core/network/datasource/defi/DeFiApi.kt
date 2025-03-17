package com.crypto.interview.core.network.datasource.defi

import com.crypto.interview.core.model.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:57<br>
 * Desc: <br>
 */
internal interface DeFiApi {
    //    @GET("defi/data")
    fun getDeFiData(): Flow<Any>
}