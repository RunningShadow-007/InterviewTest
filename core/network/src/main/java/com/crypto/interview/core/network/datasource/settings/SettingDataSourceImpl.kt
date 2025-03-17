package com.crypto.interview.core.network.datasource.settings

//import com.crypto.interview.core.network.di.NetworkModule.Companion.provideService
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.network.di.NetworkModule
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:10<br>
 * Desc: <br>
 */
//@Singleton
 class SettingDataSourceImpl private constructor() :
    SettingDataSource {

    companion object {
        @Volatile
        private var instance: SettingDataSourceImpl? = null
        fun getInstance(): SettingDataSourceImpl {
            return instance?: synchronized(this) {
                instance?: SettingDataSourceImpl().also { instance = it }
            }
        }
    }
    private val settingApi by lazy {
//        retrofit.provideService(SettingApi::class.java)
        NetworkModule.getInstance().provideService(SettingApi::class.java)
    }

    override suspend fun getSetting(): NetworkResponse<Any> {
        return settingApi.getSettings()
    }

}