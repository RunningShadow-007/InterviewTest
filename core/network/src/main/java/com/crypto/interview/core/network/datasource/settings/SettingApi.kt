package com.crypto.interview.core.network.datasource.settings

import com.crypto.interview.core.model.NetworkResponse
import retrofit2.http.GET

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:57<br>
 * Desc: <br>
 */
internal interface SettingApi {
    @GET("settings/userinfo")
  suspend  fun getSettings(): NetworkResponse<Any>
}