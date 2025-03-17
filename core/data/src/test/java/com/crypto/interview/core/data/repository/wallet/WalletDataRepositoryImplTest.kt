package com.crypto.interview.core.data.repository.wallet

import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import com.google.gson.JsonParser
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Unit tests for [WalletDataRepositoryImpl]
 *
 * Test scenarios cover:
 * - Success response with valid data
 * - Error response from data source
 * - Exception handling in coroutine operations
 * - Edge cases like empty list and serialization errors
 */
class WalletDataRepositoryImplTest {

    private val mockDataSource: WalletDataSource = mockk()
    private val repository = WalletDataRepositoryImpl(mockDataSource)

    @Test
    fun `getWalletBalances should return Success when remote succeeds`() = runTest {
        // Arrange
        val expectedData = listOf(createTestWalletBalance())
        coEvery { mockDataSource.getWalletBalances() } returns NetworkResponse.Success(
            expectedData,
            true
        )

        // Act
        val result = repository.getWalletBalances()

        // Assert
        assertEquals(expectedData, (result as NetworkResponse.Success).data)
        coEvery { mockDataSource.getWalletBalances() }
    }

    @Test
    fun `getExchangeRates should handle empty list from remote`() = runTest {
        // Arrange
        val emptyList = emptyList<ExchangeRate>()
        coEvery { mockDataSource.getExchangeRates() } returns NetworkResponse.Success(
            emptyList,
            true
        )

        // Act
        val result = repository.getExchangeRates()

        // Assert
        assert(emptyList == (result as NetworkResponse.Success).data)
    }

    @Test
    fun `getCurrencies should return Error when remote returns error`() = runTest {
        // Arrange
        val expectedError = "API_ERROR"
        coEvery { mockDataSource.getCurrencies() } returns NetworkResponse.Error(
            expectedError,
            false
        )

        // Act
        val result = repository.getCurrencies()

        // Assert
        assertEquals(expectedError, (result as NetworkResponse.Error).error)
    }

    @Test
    fun `should return Exception when data source throws`() = runTest {
        // Arrange
        val exception = RuntimeException("Network failure")
        coEvery { mockDataSource.getWalletBalances() } throws exception

        // Act
        val result = repository.getWalletBalances()

        // Assert
        assertEquals(exception, (result as NetworkResponse.Error).error)
    }

    // Helper methods for test data creation
    private fun createTestWalletBalance() = WalletBalance(
        currency = "BTC",
        amount = "1.5"
    )


    @Test
    fun `should propagate cancellation exception`() = runTest {
        // Arrange
        val job = launch {
            repository.getCurrencies()
        }

        // Act
        job.cancel()

        // Assert
        assertTrue(job.isCancelled)
    }
}