package com.crypto.interview.core.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable

/**
 * Copyright:Crypto
 * Author: liyang <br>
 * Date:2025/3/1613:30<br>
 * Desc: <br>
 */

@Serializable
sealed class NetworkResponse<out T> {
    @Serializable
    data class Success<out T>(
        val data: T,
        val ok: Boolean,
        val warning: String = ""
    ) : NetworkResponse<T>()

    @Serializable
    data class Error(
        val error: String?=null,
        val ok: Boolean,
    ) :  NetworkResponse<Nothing>()
}




