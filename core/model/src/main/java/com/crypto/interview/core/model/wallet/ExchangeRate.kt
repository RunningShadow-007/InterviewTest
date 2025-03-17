package com.crypto.interview.core.model.wallet

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:08<br>
 * Desc: <br>
 */
@Serializable
data class ExchangeRate(
    @SerializedName("from_currency")
    val fromCurrency: String,
    @SerializedName("to_currency")
    val toCurrency: String,
    val rate: Double,
    val timestamp: String
)

