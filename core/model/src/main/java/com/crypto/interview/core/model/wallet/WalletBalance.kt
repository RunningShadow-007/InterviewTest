package com.crypto.interview.core.model.wallet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:09<br>
 * Desc: <br>
 */
@Parcelize
data class WalletBalance(
    val currency: String,
    val amount: String
): Parcelable