package com.cryptofolio.feature.portfolio.overview

import app.cash.turbine.test
import com.cryptofolio.core.testing.runViewModelTest
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.PortfolioRepository
import com.cryptofolio.feature.portfolio.usecase.GetPortfolioUseCase
import com.cryptofolio.feature.portfolio.usecase.RefreshPricesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals

class PortfolioViewModelTest {

    @Test
    fun `given repository has portfolio - when initialized - then state updated with portfolio`() =
        runViewModelTest {
            // Given
            val testPortfolio = Portfolio.mock()
            val repository = FakePortfolioRepository(portfolio = testPortfolio)

            // When
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )

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
        runViewModelTest {
            // Given
            val emptyPortfolio = Portfolio.mock(assets = emptyList())
            val repository = FakePortfolioRepository(portfolio = emptyPortfolio)

            // When
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )

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
        runViewModelTest {
            // Given
            val repository = FakePortfolioRepository()
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )

            // When / Then
            viewModel.events.test {
                viewModel.onAction(PortfolioAction.SelectAsset("bitcoin"))
                assertEquals(PortfolioEvent.NavigateToAssetDetail("bitcoin"), awaitItem())
            }
        }

    @Test
    fun `given refresh fails - when RefreshPrices action - then send ShowError event`() =
        runViewModelTest {
            // Given
            val repository = FakePortfolioRepository(
                refreshResult = Result.failure(Exception("Network error")),
            )

            // When / Then
            val viewModel = PortfolioViewModel(
                getPortfolio = GetPortfolioUseCase(repository),
                refreshPrices = RefreshPricesUseCase(repository),
            )
            viewModel.events.test {
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
