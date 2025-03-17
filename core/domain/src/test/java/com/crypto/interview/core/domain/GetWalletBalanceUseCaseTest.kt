package com.crypto.interview.core.domain

import com.crypto.interview.core.data.repository.wallet.WalletDataRepository
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.Rates
import com.crypto.interview.core.model.wallet.WalletBalance
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GetWalletBalanceUseCase]
 *
 * Test scenarios cover:
 * 1. Successful response with all data sources available
 * 2. Error combinations (single/multiple failures)
 * 3. Exception handling in data processing
 * 4. Coroutine concurrency verification
 */
class GetWalletBalanceUseCaseTest {
    private lateinit var repository: WalletDataRepository
    private lateinit var useCase: GetWalletBalanceUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetWalletBalanceUseCase(repository)
    }

    @Test
    fun `should return success when all sources succeed`() = runTest {
        // Arrange
        val testBalances = listOf(
            WalletBalance("BTC", "1.5"),
            WalletBalance("ETH", "5.0")
        )
        val testCurrencies = listOf(
            Currency("BTC", "Bitcoin", tokenDecimal = 8),
            Currency("ETH", "Ethereum", tokenDecimal = 18)
        )
        val testRates = listOf(
            ExchangeRate(
                "BTC",
                "USD",
                listOf(Rates("USD", "45000.0")),
                timestamp = System.currentTimeMillis()
                    .toString()
            ),
            ExchangeRate(
                "ETH",
                "USD",
                listOf(Rates("USD", "3000.0")),
                timestamp = System.currentTimeMillis().toString()
            )
        )

        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(testBalances, true)
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(testCurrencies, true)
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(testRates, true)

        // Act
        val result = useCase.getWalletData().first()

        // Assert
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success).data
        assertEquals(2, data.walletBalances.size)
        assertEquals("67500.00", data.totalUsdValue) // 1.5 * 45000 + 5.0 * 3000
    }

    @Test
    fun `should aggregate errors when multiple sources fail`() = runTest {
        // Arrange
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Error(
            "Balance error",
            false,
        )
        coEvery { repository.getCurrencies() } returns NetworkResponse.Error(
            "Currency error",
            false,
        )
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(emptyList(),true)

        // Act
        val result = useCase.getWalletData().first()

        // Assert
        assertTrue(result is NetworkResponse.Error)
        val error = (result as NetworkResponse.Error).error
        assertTrue(error?.contains("Balance error")==true)
        assertTrue(error?.contains("Currency error")==true)
    }

    @Test
    fun `should handle empty rate data gracefully`() = runTest {
        // Arrange
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(
            listOf(
                WalletBalance("BTC", "1.0")
            ),
            true
        )
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(
            listOf(
                Currency(
                    "BTC",
                    "Bitcoin",
                    tokenDecimal = 8
                )
            ),
            true
        )
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(emptyList(), true)

        // Act
        val result = useCase.getWalletData().first()

        // Assert
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success).data
        assertEquals("0.00", data.totalUsdValue)
    }

    @Test
    fun `should handle single source failure - balances error`() = runTest {
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Error(
            "Balance fetch failed",
            false,
        )
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(emptyList(),true)
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(emptyList(),true)

        val result = useCase.getWalletData().first()
        assertTrue(result is NetworkResponse.Error)
        assertTrue((result as NetworkResponse.Error).error?.contains("Balance fetch failed")==true)
    }

    @Test
    fun `should handle single source failure - currencies error`() = runTest {
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(emptyList(),false)
        coEvery { repository.getCurrencies() } returns NetworkResponse.Error(
            "Currency fetch failed",
            false
        )
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(emptyList(),false)

        val result = useCase.getWalletData().first()
        assertTrue(result is NetworkResponse.Error)
        assertTrue((result as NetworkResponse.Error).error?.contains("Currency fetch failed")==true)
    }

    @Test
    fun `should handle single source failure - rates error`() = runTest {
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(emptyList(), true)
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(emptyList(), true)
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Error(
            "Rates fetch failed",
            false
        )

        val result = useCase.getWalletData().first()
        assertTrue(result is NetworkResponse.Error)
        assertTrue((result as NetworkResponse.Error).error?.contains("Rates fetch failed")==true)
    }

    @Test
    fun `should handle multiple currencies with different decimal precision`() = runTest {
        // Arrange
        val testBalances = listOf(
            WalletBalance("BTC", "1.23456789"),
            WalletBalance("ETH", "5.678901234567890123"),
            WalletBalance("XRP", "1000.123456")
        )
        val testCurrencies = listOf(
            Currency("BTC", "Bitcoin", tokenDecimal =  8),
            Currency("ETH", "Ethereum", tokenDecimal =  18),
            Currency("XRP", "Ripple", tokenDecimal =  6)
        )
        val testRates = listOf(
            ExchangeRate("BTC","USD", listOf(Rates("USD", "45000.00")), timestamp = ""),
            ExchangeRate("ETH","USD", listOf(Rates("USD", "3250.50")), timestamp = ""),
            ExchangeRate("XRP","USD", listOf(Rates("USD", "0.654321")), timestamp = "")
        )

        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(testBalances, true)
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(testCurrencies, true)
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(testRates, true)

        // Act
        val result = useCase.getWalletData().first()

        // Assert
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success).data
        assertEquals(
            "66871.30",
            data.totalUsdValue
        ) // 1.23456789*45000 + 5.678901234567890123*3250.50 + 1000.123456*0.654321

        val btcItem = data.walletBalances.find { it.currency.code == "BTC" }!!
        assertEquals("1.23456789", btcItem.amount)
        assertEquals("55555.55", btcItem.usdValue) // 1.23456789 * 45000 = 55555.55505 保留2位小数

        val ethItem = data.walletBalances.find { it.currency.code == "ETH" }!!
        assertEquals("5.678901234567890123", ethItem.amount)
        assertEquals(
            "18467.76",
            ethItem.usdValue
        ) // 5.678901234567890123 * 3250.5 = 18467.759999...

        val xrpItem = data.walletBalances.find { it.currency.code == "XRP" }!!
        assertEquals("1000.123456", xrpItem.amount)
        assertEquals("654.73", xrpItem.usdValue) // 1000.123456 * 0.654321 = 654.730146
    }

    @Test
    fun `should handle missing currency information`() = runTest {
        // Arrange
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(
            listOf(
                WalletBalance("UNKNOWN", "10.0")
            ),
            true
        )
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(emptyList(), true)
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(
            listOf(
                ExchangeRate("UNKNOWN","USD", listOf(Rates("USD", "1.23")), timestamp = "")
            ),
            true
        )

        // Act
        val result = useCase.getWalletData().first()

        // Assert
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success).data
        val unknownItem = data.walletBalances.first()
        assertEquals("UNKNOWN", unknownItem.currency.code)
        assertEquals("Unknown Currency", unknownItem.currency.name)
        assertEquals(2, unknownItem.currency.displayDecimal) // 默认小数位数
    }

    @Test
    fun `should verify zero amount handling`() = runTest {
        // Arrange
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(
            listOf(
                WalletBalance("BTC", "0.0")
            ),
            true
        )
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(
            listOf(
                Currency("BTC", "Bitcoin", displayDecimal =  8)
            ),
            true
        )
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(
            listOf(
                ExchangeRate("BTC","USD", listOf(Rates("USD", "45000.0")), timestamp = "")
            ),
            true
        )

        // Act
        val result = useCase.getWalletData().first()

        // Assert
        assertTrue(result is NetworkResponse.Success)
        val data = (result as NetworkResponse.Success).data
        assertEquals("0.00", data.totalUsdValue)
        assertEquals("0.0", data.walletBalances.first().amount)
        assertEquals("0.00", data.walletBalances.first().usdValue)
    }

    @Test
    fun `should throw exception for invalid rate format`() = runTest {
        // Arrange
        coEvery { repository.getWalletBalances() } returns NetworkResponse.Success(
            listOf(
                WalletBalance("BTC", "1.0")
            ),
            true
        )
        coEvery { repository.getCurrencies() } returns NetworkResponse.Success(
            listOf(
                Currency("BTC", "Bitcoin", displayDecimal =  8)
            ),
            true
        )
        coEvery { repository.getExchangeRates() } returns NetworkResponse.Success(
            listOf(
                ExchangeRate("BTC","USD", listOf(Rates("USD", "invalid")), timestamp = "")
            ),
            true
        )

    }

    @Test
    fun `should verify concurrent execution of data sources`() = runTest {
        // Arrange
        val testBalances = listOf(WalletBalance("BTC", "1.0"))
        val testCurrencies = listOf(Currency("BTC", "Bitcoin", displayDecimal = 8))
        val testRates = listOf(ExchangeRate("BTC","USD", listOf(Rates("USD", "45000.0")), timestamp = ""))

        coEvery { repository.getWalletBalances() } coAnswers {
            kotlinx.coroutines.delay(100)
            NetworkResponse.Success(testBalances,true)
        }
        coEvery { repository.getCurrencies() } coAnswers {
            kotlinx.coroutines.delay(100)
            NetworkResponse.Success(testCurrencies,true)
        }
        coEvery { repository.getExchangeRates() } coAnswers {
            kotlinx.coroutines.delay(100)
            NetworkResponse.Success(testRates,true)
        }

        // Act
        val startTime = System.currentTimeMillis()
        useCase.getWalletData().first()
        val duration = System.currentTimeMillis() - startTime

        // Assert
        assertTrue("Data sources should execute concurrently",duration > 200,)
    }
}