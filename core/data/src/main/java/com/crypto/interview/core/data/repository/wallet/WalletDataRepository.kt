package com.crypto.interview.core.data.repository.wallet

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance

import kotlinx.coroutines.flow.Flow

interface WalletDataRepository {
   suspend fun getWalletBalances(): NetworkResponse<List<WalletBalance>>
  suspend  fun getExchangeRates(): NetworkResponse<List<ExchangeRate>>
   suspend fun getCurrencies(): NetworkResponse<List<Currency>>
}