package com.crypto.interview.core.domain

import com.crypto.interview.core.common.di.CryptoDispatchers
import com.crypto.interview.core.common.di.Dispatcher
import com.crypto.interview.core.data.repository.defi.DeFiDataRepository
import com.crypto.interview.core.data.repository.setting.SettingDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 13:16<br>
 * Desc: <br>
 *
 * This is just a showcase of how to use the composite biz use case.
 *
 * Handles composite business logic by integrating multiple data sources
 * to manage presentation layer operations.
 */
@Singleton
class GetCompositeBizDataUseCase @Inject constructor(
    private val deFiDataRepository: DeFiDataRepository,
    private val settingDataRepository: SettingDataRepository,
    @Dispatcher(CryptoDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {


    operator fun invoke(): Flow<Result<Any>> {
        return combine(
            deFiDataRepository.getDeFiData(),
            settingDataRepository.getSetting()
        ) { combinedData ->
//            settingDataRepository.getSetting()
//                .map { it ->
//                    //do something with combinedData and return ui layer needed
//                    combinedData
//                    bizData
//                }
            Result.success(combinedData)
        }.flowOn(ioDispatcher)
    }

}