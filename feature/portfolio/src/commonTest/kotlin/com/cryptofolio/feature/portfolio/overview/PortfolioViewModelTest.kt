package com.cryptofolio.feature.portfolio.overview

import app.cash.turbine.test
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

@OptIn(ExperimentalCoroutinesApi::class)
class PortfolioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given repository has portfolio - when initialized - then state updated with portfolio`() =
        runTest(testDispatcher) {
            // Given
            val testPortfolio = Portfolio.mock()
            val repository = FakePortfolioRepository(portfolio = testPortfolio)

            // When
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )
            advanceUntilIdle()

            // Then
            val expected = PortfolioUiState(
                portfolio = testPortfolio,
                isLoading = false,
                isRefreshing = false,
                error = null,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given empty portfolio - when initialized - then state has empty assets`() =
        runTest(testDispatcher) {
            // Given
            val emptyPortfolio = Portfolio.mock(assets = emptyList())
            val repository = FakePortfolioRepository(portfolio = emptyPortfolio)

            // When
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )
            advanceUntilIdle()

            // Then
            val expected = PortfolioUiState(
                portfolio = emptyPortfolio,
                isLoading = false,
                isRefreshing = false,
                error = null,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given SelectAsset action - when onAction - then send NavigateToAssetDetail event`() =
        runTest(testDispatcher) {
            // Given
            val repository = FakePortfolioRepository()
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )
            advanceUntilIdle()

            // When / Then
            viewModel.events.test {
                viewModel.onAction(PortfolioAction.SelectAsset("bitcoin"))
                assertEquals(PortfolioEvent.NavigateToAssetDetail("bitcoin"), awaitItem())
            }
        }

    @Test
    fun `given refresh fails - when RefreshPrices action - then send ShowError event`() =
        runTest(testDispatcher) {
            // Given
            val repository = FakePortfolioRepository(
                refreshResult = Result.failure(Exception("Network error")),
            )

            // When / Then
            // init already calls RefreshPrices, so we collect events from the start
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )
            viewModel.events.test {
                advanceUntilIdle()
                assertEquals(PortfolioEvent.ShowError("Network error"), awaitItem())
            }
        }
}

private class FakePortfolioRepository(
    private val portfolio: Portfolio = Portfolio.mock(),
    private val refreshResult: Result<Unit> = Result.success(Unit),
) : PortfolioRepository {
    override fun getPortfolio(currency: Currency): Flow<Portfolio> = flowOf(portfolio)
    override suspend fun refreshPrices(currency: Currency): Result<Unit> = refreshResult
}
