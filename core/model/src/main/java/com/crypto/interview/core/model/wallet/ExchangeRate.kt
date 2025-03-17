package com.crypto.interview.core.model.wallet

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:08<br>
 * Desc: <br>
 */
@Parcelize
data class ExchangeRate(
    @SerializedName("from_currency")
    val fromCurrency: String,
    @SerializedName("to_currency")
    val toCurrency: String,
    val rates: List<Rates>,
    val timestamp: String
): Parcelable

@Parcelize
data class Rates(
    val amount: String,
    val rate: String
): Parcelable

