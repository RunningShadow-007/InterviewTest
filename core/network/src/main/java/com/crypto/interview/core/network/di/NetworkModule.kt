package com.crypto.interview.core.network.di


import androidx.tracing.trace
import com.crypto.interview.core.network.datasource.defi.DeFiApi
import com.crypto.interview.core.network.datasource.settings.SettingApi
import com.crypto.interview.core.network.datasource.wallet.WalletApi
import com.crypto.interview.core.network.interceptors.DynamicFieldInterceptor
import com.crypto.interview.core.network.interceptors.createNetworkResponseConverterFactory
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Copyright:Members
 * Author: liyang <br>
 * Date:2025/3/12 17:48<br>
 * Desc: <br>
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    private const val HOST = "http://123.57.210.143:8080/"
    private const val READ_TIMEOUT = 20000L
    private const val CONNECT_TIMEOUT = 15000L
    private const val WRITE_TIMEOUT = 20000L
    private const val CALL_TIMEOUT = 30000L

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient = trace("MembersOkHttpClient") {
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


    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(createNetworkResponseConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient())
            .build()
    }

    @Provides
    @Singleton
    fun providesWalletApi(): WalletApi {
        return retrofit().create(WalletApi::class.java)
    }

//    @Provides
//    @Singleton
//    fun providesDeFiApi(): DeFiApi {
//        return retrofit().create(DeFiApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun providesSettingApi(): SettingApi {
//        return retrofit().create(SettingApi::class.java)
//    }
}





