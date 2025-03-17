package com.crypto.interview.core.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/14 17:42<br>
 * Desc: <br>
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopesModule {
    @Provides
    @Singleton
    @ApplicationScope
    fun provideCoroutineScopes(@Dispatcher(CryptoDispatchers.Default) dispatcher: CoroutineDispatcher) =
        CoroutineScope(SupervisorJob() + dispatcher)
}


@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val mDispatcher: CryptoDispatchers)

enum class CryptoDispatchers {
    Default,
    IO,
}