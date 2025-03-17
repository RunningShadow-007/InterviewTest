package com.crypto.interview.core.network.datasource.defi
import com.crypto.interview.core.model.NetworkResponse

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:09<br>
 * Desc: <br>
 */
interface DeFiDataSource {
   suspend fun getDeFiData(): NetworkResponse<Any>
}