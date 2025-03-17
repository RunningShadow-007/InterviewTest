package com.crypto.interview.core.network.interceptors

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 03:38<br>
 * Desc: <br>
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
            // 解析原始JSON
            val jsonObject = JsonParser.parseString(bodyString).asJsonObject

            // 提取公共字段
            val ok = jsonObject.get("ok")?.asBoolean == true
            val warning = jsonObject.get("warning")?.asString ?: ""
            // 如果请求失败，直接返回错误响应
            if (!ok) {
                val errorResponse = NetworkResponse.Error(ok = false, error = warning)
                return response.newBuilder()
                    .body(gson.toJson(errorResponse).toResponseBody(mediaType))
                    .build()
            }

            // 找到数据字段（不是ok或warning的字段）
            val dataFieldName = jsonObject.keySet().firstOrNull { it !in setOf("ok", "warning") }
            // 如果找不到数据字段，返回错误
            if (dataFieldName == null) {
                val errorResponse = NetworkResponse.Error(ok = false, error = "无法找到数据字段")
                return response.newBuilder()
                    .body(gson.toJson(errorResponse).toResponseBody(mediaType))
                    .build()
            }

            // 构建标准化的JSON结构
            val standardizedJson = buildString {
                append("""{"ok":$ok,"warning":"$warning","data":""")
                append(jsonObject.get(dataFieldName).toString())
                append("}")
            }
            // 返回标准化的响应
            return response.newBuilder()
                .body(standardizedJson.toResponseBody(mediaType))
                .build()
        } catch (e: Exception) {
            // 处理解析异常
            val errorResponse = NetworkResponse.Error(ok = false, error = "解析响应时出错: ${e.message}")
            return response.newBuilder()
                .body(gson.toJson(errorResponse).toResponseBody(mediaType))
                .build()
        }
    }
}

/**
 * 创建处理NetworkResponse的转换器工厂
 */
fun createNetworkResponseConverterFactory(gson: Gson = Gson()): Converter.Factory {
    return object : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<okhttp3.ResponseBody, *>? {
            // 检查是否是NetworkResponse类型
            if (type is ParameterizedType && getRawType(type) == NetworkResponse::class.java) {
                // 获取NetworkResponse的泛型参数类型
                val responseType = getParameterUpperBound(0, type)
                return NetworkResponseConverter<Any>(gson, responseType)
            }

            // 对于其他类型，返回null让下一个转换器处理
            return null
        }
    }
}

/**
 * NetworkResponse专用转换器
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
                // 成功响应

                val data = gson.fromJson<T>(jsonObject.get("data").toString(), type)
                NetworkResponse.Success(ok = true, data = data)
            } else {

                // 错误响应
                val error = jsonObject.get("warning")?.asString ?: "未知错误"
                NetworkResponse.Error(ok = false, error = error)
            }
        } catch (e: Exception) {
            // 处理异常
            NetworkResponse.Error(ok = false, error = e.message ?: "未知错误")
        } finally {
            value.close()
        }
    }
}