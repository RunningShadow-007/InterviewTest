package com.crypto.interview.core.network.datasource.wallet

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import retrofit2.http.GET

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:57<br>
 * Desc: <br>
 */
internal interface WalletApi {
    @GET("familyhelp/taylor/wallet-balance")
   suspend fun getWalletBalances(): NetworkResponse<List<WalletBalance>>

    @GET("familyhelp/taylor/live-rates")
   suspend fun getExchangeRates(): NetworkResponse<List<ExchangeRate>>

    @GET("familyhelp/taylor/currencies")
   suspend fun getCurrencies():NetworkResponse<List<Currency>>
}