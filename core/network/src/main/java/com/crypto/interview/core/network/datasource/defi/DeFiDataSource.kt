package com.crypto.interview.core.network.datasource.defi
import com.crypto.interview.core.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:09<br>
 * Desc: <br>
 */
interface DeFiDataSource {
    fun getDeFiData(): Flow<Any>
}