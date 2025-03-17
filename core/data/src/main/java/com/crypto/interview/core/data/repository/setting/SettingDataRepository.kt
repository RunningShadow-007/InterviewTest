package com.crypto.interview.core.data.repository.setting


import com.crypto.interview.core.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface SettingDataRepository {
    fun getSetting(): Flow<Any>
}