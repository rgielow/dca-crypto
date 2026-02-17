package com.cryptofolio.feature.portfolio.usecase

import app.cash.turbine.test
import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPortfolioUseCaseTest {

    @Test
    fun `given repository has portfolio - when invoke - then emit portfolio`() = runTest {
        // Given
        val expected = Portfolio.mock(
            assets = listOf(
                Asset.mock(
                    totalAmount = 1.5,
                    averageBuyPrice = 45000.0,
                    totalInvested = 67500.0,
                    currentPrice = 55000.0,
                    currentValue = 82500.0,
                    profitLoss = 15000.0,
                    profitLossPercentage = 22.22,
                ),
            ),
        )
        val repository = object : PortfolioRepository {
            override fun getPortfolio(currency: Currency): Flow<Portfolio> = flowOf(expected)
            override suspend fun refreshPrices(currency: Currency): Result<Unit> = Result.success(Unit)
        }
        val useCase = GetPortfolioUseCase(repository)

        // When / Then
        useCase(Currency.USD).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `given repository has no assets - when invoke - then emit empty portfolio`() = runTest {
        // Given
        val emptyPortfolio = Portfolio.mock(assets = emptyList())
        val repository = object : PortfolioRepository {
            override fun getPortfolio(currency: Currency): Flow<Portfolio> = flowOf(emptyPortfolio)
            override suspend fun refreshPrices(currency: Currency): Result<Unit> = Result.success(Unit)
        }
        val useCase = GetPortfolioUseCase(repository)

        // When / Then
        useCase(Currency.USD).test {
            assertEquals(emptyPortfolio, awaitItem())
            awaitComplete()
        }
    }
}
