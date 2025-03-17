package com.crypto.interview.core.network.interceptors

import com.crypto.interview.core.model.NetworkResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ResponseInterceptor(private val mediaType: MediaType) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val originalBody = response.body?.string() ?: return response

        val jsonObj = Json.parseToJsonElement(originalBody).jsonObject
        val (ok, warning) = getCommonFields(jsonObj)

        // 处理错误响应
        if (!ok) {
            return response.newBuilder()
                .body(
                    Json.encodeToString(NetworkResponse.Error(warning, ok))
                        .toResponseBody("application/json".toMediaType())
                )
                .build()
        }

        // 构建成功响应
        val modifiedJson = buildJsonObject {
            put("type", JsonPrimitive("NetworkResponse.Success"))
            put("ok", JsonPrimitive(ok))
            put("warning", JsonPrimitive(warning))
            // 自动处理 data 字段
            jsonObj.forEach { (key, value) ->
                if (key !in setOf("ok", "warning")) {
                    put("data", value)
                }
            }
        }.toString()

        return response.newBuilder()
            .body(modifiedJson.toResponseBody("application/json".toMediaType()))
            .build()
    }
}

private fun getCommonFields(json: JsonObject): Pair<Boolean, String> {
    val ok = json["ok"]?.jsonPrimitive?.boolean == true
    val warning = json["warning"]?.jsonPrimitive?.contentOrNull ?: ""
    return Pair(ok, warning)
}

fun Json.asConverterFactory(): Converter.Factory =
    object : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<okhttp3.ResponseBody, *>? {
            return this@asConverterFactory
                .serializersModule
                .serializer(type)
                .let { JsonResponseBodyConverter(this@asConverterFactory, it) }
        }

        private inner class JsonResponseBodyConverter<T>(
            private val json: Json,
            private val serializer: KSerializer<T>
        ) : Converter<okhttp3.ResponseBody, T> {
            override fun convert(value: okhttp3.ResponseBody): T {
                return json.decodeFromString(serializer, value.string())
            }
        }
    }







