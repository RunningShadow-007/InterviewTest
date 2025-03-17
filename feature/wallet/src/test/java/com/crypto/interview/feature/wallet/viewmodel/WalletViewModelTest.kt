package com.crypto.interview.feature.wallet.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.crypto.interview.core.domain.GetWalletBalanceUseCase
import com.crypto.interview.core.model.NetworkResponse
import com.crypto.interview.core.model.wallet.Currency
import com.crypto.interview.core.model.wallet.ExchangeRate
import com.crypto.interview.core.model.wallet.WalletBalance
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WalletViewModelTest {
    private val testBalances = listOf(
        WalletBalance("BTC", "0.0026"),
        WalletBalance("ETH", "1.5"),
        WalletBalance("CRO", "1000.0")
    )

    private val testCurrencies = listOf(
        Currency("BTC", "Bitcoin", 8),
        Currency("ETH", "Ethereum", 18),
        Currency("CRO", "Crypto.com Coin", 8)
    )

    private val testRates = listOf(
        ExchangeRate("BTC", listOf(Rates("USD", "9194.93"))),
        ExchangeRate("ETH", listOf(Rates("USD", "3000.00"))),
        ExchangeRate("CRO", listOf(Rates("USD", "0.50")))
    )

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val useCase: GetWalletBalanceUseCase = mockk()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should_show_loading_state_initially`() = runTest {
        // Arrange
        coEvery { useCase.getWalletData() } returns NetworkResponse.Success(fakeSuccessData(), true)
        
        // Act
        val viewModel = WalletViewModel(useCase)
        
        // Assert
        assertTrue(viewModel.walletUiState.first() is WalletViewModel.WalletUiState.Loading)
    }

    @Test
    fun `should_show_success_state_with_correct_formatting`() = runTest {
        // Arrange
        coEvery { useCase.getWalletData() } returns NetworkResponse.Success(fakeSuccessData(), true)
        
        // Act
        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Success }
        
        // Assert
        val successState = state as WalletViewModel.WalletUiState.Success
        assertEquals("3", successState.data.walletBalances.size)
        assertEquals("23,906.82", successState.data.totalUsdValue)
        
        val btcItem = successState.data.walletBalances.find { it.currency.code == "BTC" }
        assertEquals("0.00260000", btcItem?.amount)
        assertEquals("23,906.82", btcItem?.usdValue)
    }

    @Test
    fun `should_show_error_state_when_network_fails`() = runTest {
        // Arrange
        coEvery { useCase.getWalletData() } returns NetworkResponse.Error(
            isNetworkError = true,
            error = "Network unavailable"
        )
        
        // Act
        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Failed }
        
        // Assert
        val errorState = state as WalletViewModel.WalletUiState.Failed
        assertEquals("Network unavailable", errorState.error)
    }

    @Test
    fun `should_handle_empty_balance_with_rates`() = runTest {
        // Arrange
        coEvery { useCase.getWalletData() } returns NetworkResponse.Success(
            WalletData(
                walletBalances = emptyList(),
                totalUsdValue = "0.00"
            ),
            true
        )
        
        // Act
        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Success }
        
        // Assert
        val successState = state as WalletViewModel.WalletUiState.Success
        assertEquals("0.00", successState.data.totalUsdValue)
        assertTrue(successState.data.walletBalances.isEmpty())
    }

    @Test
    fun `should_format_currency_with_correct_decimals`() = runTest {
        // Arrange
        coEvery { useCase.getWalletData() } returns NetworkResponse.Success(
            WalletData(
                walletBalances = listOf(
                    CurrencyItem(
                        currency = Currency("BTC", "Bitcoin", 8),
                        amount = "0.12345678",
                        usdValue = "1234.56"
                    ),
                    CurrencyItem(
                        currency = Currency("ETH", "Ethereum", 18),
                        amount = "1.234567890123456789",
                        usdValue = "2345.67"
                    )
                ),
                totalUsdValue = "3579.23"
            ),
            true
        )
        
        // Act
        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Success }
        
        // Assert
        val successState = state as WalletViewModel.WalletUiState.Success
        assertEquals("0.12345678", successState.data.walletBalances[0].amount)
        assertEquals("1.234567890123456789", successState.data.walletBalances[1].amount)
    }

    @Test
    fun `should_handle_concurrent_data_fetching`() = runTest {
        coEvery { useCase.getWalletData() } coAnswers {
            kotlinx.coroutines.delay(100)
            NetworkResponse.Success(fakeSuccessData())
        }

        val startTime = System.currentTimeMillis()
        WalletViewModel(useCase).walletUiState.first { it is WalletViewModel.WalletUiState.Success }
        val duration = System.currentTimeMillis() - startTime

        assertTrue(duration < 150, "Data fetching should complete within 150ms, took $duration ms")
    }

    @Test
    fun `should_aggregate_partial_errors`() = runTest {
        coEvery { useCase.getWalletData() } returns NetworkResponse.Error(
            isNetworkError = false,
            error = "Balance sync failed, Rates update timeout"
        )

        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Failed }

        val errorState = state as WalletViewModel.WalletUiState.Failed
        assertTrue(errorState.error.contains("Balance sync failed"))
        assertTrue(errorState.error.contains("Rates update timeout"))
    }

    @Test
    fun `should_handle_negative_exchange_rate`() = runTest {
        coEvery { useCase.getWalletData() } returns NetworkResponse.Success(
            WalletData(
                walletBalances = listOf(CurrencyItem(
                    currency = Currency("BTC", "Bitcoin", 8),
                    amount = "1.0",
                    usdValue = "-9194.93"
                )),
                totalUsdValue = "-9194.93"
            )
        )

        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Success }

        val successState = state as WalletViewModel.WalletUiState.Success
        assertEquals("-9,194.93", successState.data.totalUsdValue)
    }

    @Test
    fun `should_handle_missing_currency_info`() = runTest {
        coEvery { useCase.getWalletData() } returns NetworkResponse.Success(
            WalletData(
                walletBalances = listOf(CurrencyItem(
                    currency = Currency("UNKNOWN", "Unknown Currency", 2),
                    amount = "10.0",
                    usdValue = "12.30"
                )),
                totalUsdValue = "12.30"
            )
        )

        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Success }

        val successState = state as WalletViewModel.WalletUiState.Success
        assertEquals("UNKNOWN", successState.data.walletBalances[0].currency.code)
        assertEquals(2, successState.data.walletBalances[0].currency.displayDecimal)
    }

    @Test
    fun `should_format_large_numbers_correctly`() = runTest {
        coEvery { useCase.getWalletData() } returns NetworkResponse.Success(
            WalletData(
                walletBalances = listOf(CurrencyItem(
                    currency = Currency("BTC", "Bitcoin", 8),
                    amount = "1000000.12345678",
                    usdValue = "9194930000.00"
                )),
                totalUsdValue = "9,194,930,000.00"
            )
        )

        val viewModel = WalletViewModel(useCase)
        val state = viewModel.walletUiState.first { it is WalletViewModel.WalletUiState.Success }

        val successState = state as WalletViewModel.WalletUiState.Success
        assertEquals("1,000,000.12345678", successState.data.walletBalances[0].amount)
        assertEquals("9,194,930,000.00", successState.data.totalUsdValue)
    }

    private fun fakeSuccessData() = WalletData(
        walletBalances = listOf(
            CurrencyItem(
                currency = Currency("BTC", "Bitcoin", 8),
                amount = "0.0026",
                usdValue = "23,906.82"
            ),
            CurrencyItem(
                currency = Currency("ETH", "Ethereum", 18),
                amount = "1.5",
                usdValue = "4,500.00"
            ),
            CurrencyItem(
                currency = Currency("CRO", "Crypto.com Coin", 8),
                amount = "1000.0",
                usdValue = "500.00"
            )
        ),
        totalUsdValue = "23,906.82"
    )
}