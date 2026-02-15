package com.cryptofolio.feature.portfolio.overview

import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.PortfolioRepository
import com.cryptofolio.feature.portfolio.usecase.GetPortfolioUseCase
import com.cryptofolio.feature.portfolio.usecase.RefreshPricesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val testAssets = listOf(
        Asset(
            coinId = "bitcoin",
            coinName = "Bitcoin",
            coinSymbol = "BTC",
            totalAmount = 1.0,
            averageBuyPrice = 50000.0,
            totalInvested = 50000.0,
            currentPrice = 60000.0,
            currentValue = 60000.0,
            profitLoss = 10000.0,
            profitLossPercentage = 20.0,
        ),
    )

    private val testPortfolio = Portfolio(assets = testAssets)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads portfolio on init`() = runTest(testDispatcher) {
        val repository = object : PortfolioRepository {
            override fun getPortfolio(currency: Currency): Flow<Portfolio> = flowOf(testPortfolio)
            override suspend fun refreshPrices(currency: Currency): Result<Unit> = Result.success(Unit)
        }

        val viewModel = PortfolioViewModel(
            getPortfolio = GetPortfolioUseCase(repository),
            refreshPrices = RefreshPricesUseCase(repository),
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.portfolio.assets.size)
        assertEquals("bitcoin", state.portfolio.assets.first().coinId)
        assertEquals(60000.0, state.portfolio.totalCurrentValue)
    }
}
