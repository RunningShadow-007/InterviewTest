package com.crypto.interview.feature.wallet.viewmodel

import com.crypto.interview.core.domain.GetWalletBalancesException
import com.crypto.interview.core.domain.model.WalletData

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 17:25<br>
 * Desc: <br>
 */
sealed interface WalletUiState {
    data object Loading : WalletUiState
    data class Failed(val error: String) : WalletUiState
    data class Success(val walletData: WalletData) : WalletUiState
}