package com.crypto.interview.core.network.di

import androidx.tracing.trace
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.network.interceptors.DynamicFieldInterceptor
import com.crypto.interview.core.network.interceptors.ResponseInterceptor
import com.crypto.interview.core.network.interceptors.asConverterFactory
import com.crypto.interview.core.network.interceptors.createNetworkResponseConverterFactory
//import com.crypto.interview.core.network.interceptors.createGsonConverterFactory
import com.google.gson.GsonBuilder
import kotlinx.serialization.builtins.serializer
//import com.google.gson.GsonBuilder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Copyright:Members
 * Author: liyang <br>
 * Date:2025/3/12 17:48<br>
 * Desc: <br>
 */
//@Module
//@InstallIn(SingletonComponent::class)
internal class NetworkModule private constructor() {
    companion object {
        private val serviceMap by lazy {
            mutableMapOf<String, Any>()
        }
        private const val HOST = "http://123.57.210.143:8080/"
        private const val READ_TIMEOUT = 20000L
        private const val CONNECT_TIMEOUT = 15000L
        private const val WRITE_TIMEOUT = 20000L
        private const val CALL_TIMEOUT = 30000L

        @Volatile
        private var INSTANCE: NetworkModule? = null

        fun getInstance(): NetworkModule {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = NetworkModule()
                    }
                }
            }
            return INSTANCE!!
        }


        private fun okHttpClient(): OkHttpClient = trace("MembersOkHttpClient") {
            OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .callTimeout(CALL_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(DynamicFieldInterceptor("application/json".toMediaType()))
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        },
                )
                .build()
        }

        @Volatile
        private var retrofit: Retrofit? = null

        //        @Provides
//        @Singleton
        private fun retrofit(): Retrofit{
            return if (retrofit == null) {
                val gson = GsonBuilder().setLenient().create()
//                val json = Json {
//                    ignoreUnknownKeys = true
//                    explicitNulls = false
//                    coerceInputValues = true
//                    serializersModule = SerializersModule {
//                        // 这里注册泛型的反序列化器
//                        polymorphic(NetworkResponse::class, NetworkResponse.Success::class, NetworkResponse.Success.serializer())
//                        polymorphic(NetworkResponse::class, NetworkResponse.Error::class, NetworkResponse.Error.serializer())
//                    }
//                }
                retrofit = Retrofit.Builder()
                    .baseUrl(HOST)
                    .addConverterFactory(createNetworkResponseConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .client(okHttpClient())
                    .build()
                retrofit!!
            } else {
                retrofit!!
            }
        }

    }

//         fun <T> Retrofit.provideService(clazz: Class<T>): T {
//            var t: T? = null
//            t = if (serviceMap[clazz.simpleName] != null) {
//                serviceMap[clazz.simpleName] as T
//            } else {
//                create(clazz)
//            }
//            return t!!
//        }

    fun <T> provideService(clazz: Class<T>): T {

        var t: T? = null
        t = if (serviceMap[clazz.simpleName] != null) {
            serviceMap[clazz.simpleName] as T
        } else {
            retrofit().create(clazz)
        }
        return t!!
    }

}


//    @Binds
//    @Singleton
//    abstract fun bindWalletDataSource(impl: WalletDataSourceImpl): WalletDataSource
//    @Binds
//    @Singleton
//    abstract fun bindSettingDataSource(settingDataSource: SettingDataSourceImpl): SettingDataSource
//    @Binds
//    @Singleton
//    abstract fun bindDeFiDataSource(deFiDataSourceImpl: DeFiDataSourceImpl): DeFiDataSource
//}
