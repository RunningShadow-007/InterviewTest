package com.crypto.interview.core.network.datasource.wallet

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

class WalletApiTest {
    private val retrofit = mockk<Retrofit>()
    private val walletApi = mockk<WalletApi>()

    init {
        every { retrofit.create(WalletApi::class.java) } returns walletApi
    }

    @Test
    fun `getWalletBalances should call correct endpoint`() = runTest {
        // 模拟成功响应
        val mockResponse = listOf(WalletBalance("BTC", "1.0"))
        coEvery { walletApi.getWalletBalances() } returns NetworkResponse.Success<List<WalletBalance>>(mockResponse,true)

        // 执行并验证
        val result = walletApi.getWalletBalances()
        assertTrue(result is NetworkResponse.Success)
        coVerify { walletApi.getWalletBalances() }
    }

    @Test
    fun `getExchangeRates should parse error response`() = runBlocking {
        // 模拟500错误
        coEvery { walletApi.getExchangeRates() } returns NetworkResponse.Error( "Internal Server Error",false,)

        // 执行并验证
        val result = walletApi.getExchangeRates() as NetworkResponse.Error
        assert(result.error?.contains("Internal Server Error")==true)
    }

    @Test
    fun `getCurrencies should handle network exception`() = runTest {
        // 模拟网络异常
        coEvery { walletApi.getCurrencies() } throws RuntimeException("Network error")

        // 执行并验证异常
        try {
            walletApi.getCurrencies()
        } catch (e: Exception) {
            assert(e.message == "Network error")
        }
    }
}