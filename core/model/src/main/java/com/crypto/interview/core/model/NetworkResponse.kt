package com.crypto.interview.core.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Copyright:Crypto
 * Author: liyang <br>
 * Date:2025/3/1613:30<br>
 * Desc: <br>
 */

@Parcelize
sealed class NetworkResponse<out T> : Parcelable{
    @Parcelize
    data class Success<out T>(
        val data:  @RawValue T,
        val ok: Boolean,
        val warning: String = ""
    ) : NetworkResponse<T>()

    @Parcelize
    data class Error(
        val error: String?=null,
        val ok: Boolean,
    ) :  NetworkResponse<Nothing>()
}




