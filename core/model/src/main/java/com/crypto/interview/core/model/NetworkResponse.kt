package com.crypto.interview.core.model
import com.crypto.interview.core.model.NetworkResponse.Success
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Copyright:Crypto
 * Author: liyang <br>
 * Date:2025/3/1613:30<br>
 * Desc: <br>
 */

@Serializable
sealed class NetworkResponse<out T> {
    data class Success<out T>(
        val data: T,
        val ok: Boolean,
        val warning: String = ""
    ) : NetworkResponse<T>()

    @Serializable
    data class Error(
        val error: String?=null,
        val ok: Boolean,
    ) : NetworkResponse<Nothing>()
}




