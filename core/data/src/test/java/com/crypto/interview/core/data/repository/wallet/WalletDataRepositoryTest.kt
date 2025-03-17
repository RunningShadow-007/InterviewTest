package com.crypto.interview.core.data.repository.wallet

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class WalletDataRepositoryTest {
    private val mockDataSource = mockk<WalletDataSource>()
    private val repository = WalletDataRepositoryImpl(mockDataSource)

    @Test
    fun getWalletBalances_success() = runTest {
        // Arrange
        val mockData = listOf(WalletBalance("BTC", "1.0"))
        coEvery { mockDataSource.getWalletBalances() } returns NetworkResponse.Success(mockData, true)

        // Act
        val result = repository.getWalletBalances()

        // Assert
        assertTrue(result is NetworkResponse.Success)
    }

    @Test
    fun getCurrencies_success() = runTest {
        // Arrange
        val mockData = listOf(Currency("BTC", "Bitcoin"))
        coEvery { mockDataSource.getCurrencies() } returns NetworkResponse.Success(mockData, true)

        // Act
        val result = repository.getCurrencies()

        // Assert
        assertTrue(result is NetworkResponse.Success)
    }

    @Test
    fun getExchangeRates_emptyList() = runTest {
        // Arrange
        coEvery { mockDataSource.getExchangeRates() } returns NetworkResponse.Success(emptyList(), true)

        // Act
        val result = repository.getExchangeRates()

        // Assert
        assertTrue(result is NetworkResponse.Success)
    }

    @Test
    fun getExchangeRates_networkException() = runTest {
        // Arrange
        coEvery { mockDataSource.getExchangeRates() } throws RuntimeException("Network failure")

        // Act
        val result = repository.getExchangeRates()

        // Assert
        assertTrue(result is NetworkResponse.Error)
        assertTrue((result as NetworkResponse.Error).error?.contains("Network failure")==true)
    }

    @Test
    fun getCurrencies_malformedResponse() = runTest {
        // Arrange
        coEvery { mockDataSource.getCurrencies() } returns NetworkResponse.Error("Invalid format",false)

        // Act
        val result = repository.getCurrencies()

        // Assert
        assertTrue(result is NetworkResponse.Error)
        assertEquals("Invalid format", (result as NetworkResponse.Error).error)
    }
}

// 假设的Repository实现（需根据实际实现调整）
class WalletDataRepositoryImpl(private val dataSource: WalletDataSource) : WalletDataRepository {
    override suspend fun getWalletBalances() = dataSource.getWalletBalances()
    override suspend fun getExchangeRates() = dataSource.getExchangeRates()
    override suspend fun getCurrencies() = dataSource.getCurrencies()
}

// 假设的数据源接口（需与实现保持一致）
interface WalletDataSource {
    suspend fun getWalletBalances(): NetworkResponse<List<WalletBalance>>
    suspend fun getExchangeRates(): NetworkResponse<List<ExchangeRate>>
    suspend fun getCurrencies(): NetworkResponse<List<Currency>>
}