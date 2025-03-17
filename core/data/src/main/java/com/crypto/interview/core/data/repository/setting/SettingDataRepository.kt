package com.crypto.interview.core.data.repository.setting


import com.crypto.interview.core.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface SettingDataRepository {
   suspend fun getSetting(): NetworkResponse<Any>
}