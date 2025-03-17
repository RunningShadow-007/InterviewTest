package com.crypto.interview.core.domain.model

import com.crypto.interview.core.model.wallet.Currency
import java.math.BigDecimal

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 15:03<br>
 * Desc: <br>
 */
data class WalletData(val totalUsdValue: String = "0.0",
                      val walletBalances: List<CurrencyItem> = emptyList())


data class CurrencyItem(
    val currency: Currency,
    val amount: String,
    val usdValue: String
)