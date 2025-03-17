package com.crypto.interview.core.domain

import com.crypto.interview.core.data.repository.defi.DeFiDataRepository
import com.crypto.interview.core.data.repository.setting.SettingDataRepository
import com.crypto.interview.core.data.repository.wallet.WalletDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 13:16<br>
 * Desc: <br>
 * This is just a showcase of how to use the composite biz use case.
 *
 * Handles composite business logic by integrating multiple data sources
 * to manage presentation layer operations.
 */
class GetCompositeBizDataUseCase  constructor(
    private val deFiDataRepository: DeFiDataRepository,
    private val walletDataRepository: WalletDataRepository,
    private val settingDataRepository: SettingDataRepository
) {
    /*
          operator fun invoke(): Flow<Result<Any>> {
              return combine(
                  deFiDataRepository.getSetting(),
                  walletDataRepository.getWalletBalances(),
              ){combinedData->
                  settingDataRepository.getSetting()
                      .map {it->
                         //do something with combinedData and return ui layer needed
                         combinedData
                         bizData
                      }
              }
          }
      */
}