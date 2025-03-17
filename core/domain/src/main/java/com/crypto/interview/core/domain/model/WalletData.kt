package com.crypto.interview.core.domain.model

import com.crypto.interview.core.model.wallet.Currency

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 15:03<br>
 * Desc: <br>
 */
data class WalletData(val totalUsdValue: Double = 0.0,
                      val walletBalances: List<CurrencyItem> = emptyList())


data class CurrencyItem(
    val currency: Currency,
    val balance: Double,
    val usdValue: Double
)