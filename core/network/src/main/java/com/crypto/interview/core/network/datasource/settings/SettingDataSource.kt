package com.crypto.interview.core.network.datasource.settings


import com.crypto.interview.core.model.NetworkResponse
import retrofit2.http.GET

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:09<br>
 * Desc: <br>
 */
interface SettingDataSource {
    @GET("setting/info")
   suspend fun getSetting(): NetworkResponse<Any>
}