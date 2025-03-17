package com.crypto.interview.core.model.wallet

import kotlinx.serialization.Serializable

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:09<br>
 * Desc: <br>
 */
@Serializable
data class WalletBalance(
    val currency: String,
    val amount: String
)