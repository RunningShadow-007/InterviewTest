package com.crypto.interview.core.common.maths

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 11:24<br>
 * Desc: <br>
 */

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * BigDecimal计算工具类
 * 用于处理高精度数值计算，特别是加密货币金额和汇率的乘法运算
 */
object BigDecimalUtils {

    /**
     * 计算两个BigDecimal字符串的乘积
     *
     * @param amount 金额字符串
     * @param rate 汇率字符串
     * @param scale 小数位数，默认为8
     * @param roundingMode 舍入模式，默认为HALF_UP（四舍五入）
     * @return 计算结果字符串，最多保留指定小数位数，不足不补0
     */
    fun multiply(
        amount: String,
        rate: String,
        scale: Int = 8,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        try {
            val amountDecimal = BigDecimal(amount)
            val rateDecimal = BigDecimal(rate)

            // 执行乘法运算
            val result = amountDecimal.multiply(rateDecimal)

            // 设置精度和舍入模式
            val scaledResult = result.setScale(scale, roundingMode)

            // 去除末尾多余的0
            return scaledResult.stripTrailingZeros().toPlainString()
        } catch (e: NumberFormatException) {
            // 处理输入格式错误
            return "0"
        } catch (e: Exception) {
            // 处理其他异常
            return "0"
        }
    }

    /**
     * 计算BigDecimal字符串集合的总和
     *
     * @param values 需要求和的BigDecimal字符串集合
     * @param scale 结果保留的小数位数，默认为8
     * @param roundingMode 舍入模式，默认为HALF_UP（四舍五入）
     * @return 总和的字符串表示，不足指定小数位不补0
     */
    fun sum(
        values: List<String>,
        scale: Int = 8,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        if (values.isEmpty()) return "0"

        // 使用BigDecimal.ZERO作为初始值，避免创建不必要的对象
        return values
            .fold(BigDecimal.ZERO) { acc, value ->
                try {
                    // 将字符串转换为BigDecimal并累加
                    acc.add(BigDecimal(value))
                } catch (e: NumberFormatException) {
                    // 如果转换失败，忽略该值并返回当前累加结果
                    acc
                }
            }
            // 设置精度和舍入模式
            .setScale(scale, roundingMode)
            // 去除末尾多余的0
            .stripTrailingZeros()
            .toPlainString()
    }

    /**
     * 计算加密货币的美元价值
     *
     * @param cryptoAmount 加密货币数量
     * @param usdRate 美元汇率
     * @return 美元价值字符串
     */
    fun calculateUsdValue(
        cryptoAmount: String,
        usdRate: String,
        scale: Int = 8,
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): String {
        return multiply(cryptoAmount, usdRate)
    }




    /**
     * 计算钱包中所有币种余额的总和（美元价值）
     *
     * @param walletCoinBalanceList 钱包中各币种余额的美元价值列表
     * @return 总余额的美元价值
     */
    fun calculateTotalBalance(walletCoinBalanceList: List<String>): String {
        return sum(walletCoinBalanceList)
    }
}

fun String.toDoubleWithPrecision(): Double {
    return try {
        java.math.BigDecimal(this).toDouble()
    } catch (e: NumberFormatException) {
        0.0
    }
}