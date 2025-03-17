package com.crypto.interview.core.network.interceptors

/**
 * Copyright: InterviewTest
 * Author: liyang <br>
 * Date: 2025/3/17 03:38<br>
 * Desc: Interceptor for dynamically processing API response fields.<br>
 */

import com.crypto.interview.core.model.NetworkResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class DynamicFieldInterceptor(private val mediaType: MediaType) : Interceptor {
    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val originalBody = response.body ?: return response
        val bodyString = originalBody.string()

        try {
            // Parse original JSON
            val jsonObject = JsonParser.parseString(bodyString).asJsonObject

            // Extract common fields
            val ok = jsonObject.get("ok")?.asBoolean == true
            val warning = jsonObject.get("warning")?.asString ?: ""
            // Return error response immediately if request fails
            if (!ok) {
                val errorResponse = NetworkResponse.Error(ok = false, error = warning)
                return response.newBuilder()
                    .body(gson.toJson(errorResponse).toResponseBody(mediaType))
                    .build()
            }

            // Find data field (fields other than ok or warning)
            val dataFieldName = jsonObject.keySet().firstOrNull { it !in setOf("ok", "warning") }
            // Return error if data field not found
            if (dataFieldName == null) {
                val errorResponse =
                    NetworkResponse.Error(ok = false, error = "Data field not found")
                return response.newBuilder()
                    .body(gson.toJson(errorResponse).toResponseBody(mediaType))
                    .build()
            }

            // Build standardized JSON structure
            val standardizedJson = buildString {
                append("""{"ok":$ok,"warning":"$warning","data":""")
                append(jsonObject.get(dataFieldName).toString())
                append("}")
            }
            // Return standardized response
            return response.newBuilder()
                .body(standardizedJson.toResponseBody(mediaType))
                .build()
        } catch (e: Exception) {
            // Handle parsing exceptions
            val errorResponse =
                NetworkResponse.Error(ok = false, error = "Error parsing response: ${e.message}")
            return response.newBuilder()
                .body(gson.toJson(errorResponse).toResponseBody(mediaType))
                .build()
        }
    }
}

/**
 * Factory for creating NetworkResponse converter
 */
fun createNetworkResponseConverterFactory(gson: Gson = Gson()): Converter.Factory {
    return object : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<okhttp3.ResponseBody, *>? {
            // Check if it's NetworkResponse type
            if (type is ParameterizedType && getRawType(type) == NetworkResponse::class.java) {
                // Get generic parameter type of NetworkResponse
                val responseType = getParameterUpperBound(0, type)
                return NetworkResponseConverter<Any>(gson, responseType)
            }

            // Return null for other types to let next converter handle
            return null
        }
    }
}

/**
 * Converter dedicated to NetworkResponse
 */
private class NetworkResponseConverter<T>(
    private val gson: Gson,
    private val type: Type
) : Converter<okhttp3.ResponseBody, NetworkResponse<T>> {
    override fun convert(value: okhttp3.ResponseBody): NetworkResponse<T> {
        return try {
            val jsonString = value.string()
            val jsonObject = JsonParser.parseString(jsonString).asJsonObject
            val ok = jsonObject.get("ok")?.asBoolean == true

            if (ok) {
                // Success response

                val data = gson.fromJson<T>(jsonObject.get("data").toString(), type)
                NetworkResponse.Success(ok = true, data = data)
            } else {

                // Error response
                val error = jsonObject.get("warning")?.asString ?: "Unknown error"
                NetworkResponse.Error(ok = false, error = error)
            }
        } catch (e: Exception) {
            // Handle exceptions
            NetworkResponse.Error(ok = false, error = e.message ?: "Unknown error")
        } finally {
            value.close()
        }
    }
}