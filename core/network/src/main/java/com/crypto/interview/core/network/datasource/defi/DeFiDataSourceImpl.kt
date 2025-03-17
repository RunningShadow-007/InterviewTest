package com.crypto.interview.core.network.datasource.defi

//import com.crypto.interview.core.network.di.NetworkModule.Companion.provideService
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.network.di.NetworkModule
import kotlinx.coroutines.flow.Flow
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
 class DeFiDataSourceImpl private constructor() :
    DeFiDataSource {

    companion object {
        @Volatile
        private var instance: DeFiDataSourceImpl? = null
        fun getInstance(): DeFiDataSourceImpl {
            return instance?: synchronized(this) {
                instance?: DeFiDataSourceImpl().also { instance = it }
            }
        }
    }
    private val defiApi by lazy {
//        retrofit.provideService(DeFiApi::class.java)
        NetworkModule.getInstance().provideService(DeFiApi::class.java)
    }


    override suspend fun getDeFiData(): NetworkResponse<Any>{
        return defiApi.getDeFiData()
    }
}