package com.crypto.interview.core.network.datasource.wallet

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import kotlinx.coroutines.flow.Flow

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 01:09<br>
 * Desc: <br>
 */
interface WalletDataSource {
   suspend fun getWalletBalances(): NetworkResponse<List<WalletBalance>>

   suspend fun getExchangeRates(): NetworkResponse<List<ExchangeRate>>

   suspend fun getCurrencies(): NetworkResponse<List<Currency>>
}