package com.crypto.interview.feature.wallet.viewmodel

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:03<br>
 * Desc: <br>
 */
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.interview.core.data.repository.wallet.WalletDataRepository
import com.crypto.interview.core.domain.GetWalletBalanceUseCase
import com.crypto.interview.core.domain.model.CurrencyItem
import com.crypto.interview.core.domain.model.WalletData
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
//import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(getWalletData: GetWalletBalanceUseCase) : ViewModel() {
    val walletUiState = getWalletData
        .getWalletData()
        .map { result ->
            when (result) {
                is NetworkResponse.Success -> {
                    WalletUiState.Success(result.data)
                }

                is NetworkResponse.Error -> {
                    WalletUiState.Failed(result.error?:"Unknown Error")
                }
            }
        }
        .flowOn(Dispatchers.IO)
        .catch {
            emit(WalletUiState.Failed(it.message ?: "Unknown Error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000, 0),
            initialValue = WalletUiState.Loading
        )


}