
package com.crypto.interview.core.data.repository.defi

import com.crypto.interview.core.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface DeFiDataRepository {
    fun getDeFiData(): Flow<NetworkResponse<Any>>
}