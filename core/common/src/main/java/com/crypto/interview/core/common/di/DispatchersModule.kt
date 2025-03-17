package com.crypto.interview.core.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/14 17:50<br>
 * Desc: <br>
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @Dispatcher(CryptoDispatchers.Default)
    fun provideDefaultDispatchers(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(CryptoDispatchers.IO)
    fun provideIODispatchers(): CoroutineDispatcher = Dispatchers.IO

}