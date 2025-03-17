package com.crypto.interview.core.data.di


import com.crypto.interview.core.data.repository.defi.DeFiDataRepository
import com.crypto.interview.core.data.repository.defi.DeFiDataRepositoryImpl
import com.crypto.interview.core.data.repository.setting.SettingDataRepository
import com.crypto.interview.core.data.repository.setting.SettingDataRepositoryImpl
import com.crypto.interview.core.data.repository.wallet.WalletDataRepository
import com.crypto.interview.core.data.repository.wallet.WalletDataRepositoryImpl
import com.crypto.interview.core.network.datasource.defi.DeFiDataSource
import com.crypto.interview.core.network.datasource.settings.SettingDataSource
import com.crypto.interview.core.network.datasource.settings.SettingDataSourceImpl
import com.crypto.interview.core.network.datasource.wallet.WalletDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Copyright:Members
 * Author: liyang <br>
 * Date:2025/3/16 21:44<br>
 * Desc: <br>
 */
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class DataModule {
//
//    @Binds
//    @Singleton
//    abstract fun bindWalletDataRepository(impl: WalletDataRepositoryImpl): WalletDataRepository
//
//    // 其他绑定保持原样...
//    @Binds
//    @Singleton
//    abstract fun bindDeFiDataRepository(impl: DeFiDataRepositoryImpl): DeFiDataRepository
//
//    @Binds
//    @Singleton
//    abstract fun bindSettingDataRepository(impl: SettingDataRepositoryImpl): SettingDataRepository
//}

 object DataModule {
     fun provideWalletDataRepository(): WalletDataRepository= WalletDataRepositoryImpl.getInstance()


     fun provideDeFiDataRepository(): DeFiDataRepository= DeFiDataRepositoryImpl.getInstance()


     fun provideSettingDataRepository(): SettingDataRepository= SettingDataRepositoryImpl.getInstance()
}