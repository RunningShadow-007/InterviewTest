package com.crypto.interview.core.network.di

import com.crypto.interview.core.network.datasource.defi.DeFiDataSource
import com.crypto.interview.core.network.datasource.defi.DeFiDataSourceImpl
import com.crypto.interview.core.network.datasource.settings.SettingDataSource
import com.crypto.interview.core.network.datasource.settings.SettingDataSourceImpl
import com.crypto.interview.core.network.datasource.wallet.WalletDataSource
import com.crypto.interview.core.network.datasource.wallet.WalletDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 13:38<br>
 * Desc: <br>
 */
@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {
    @Singleton
    @Binds
    fun provideWalletDataSource(walletDataSourceImpl: WalletDataSourceImpl): WalletDataSource

    @Singleton
    @Binds
    fun provideDeFiDataSource(deFiDataSourceImpl: DeFiDataSourceImpl): DeFiDataSource

    @Singleton
    @Binds
    fun provideSettingDataSource(settingDataSourceImpl: SettingDataSourceImpl): SettingDataSource
}