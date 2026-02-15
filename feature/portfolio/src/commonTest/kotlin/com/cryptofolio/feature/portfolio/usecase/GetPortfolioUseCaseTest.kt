package com.cryptofolio.feature.portfolio.usecase

import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPortfolioUseCaseTest {

    @Test
    fun `returns portfolio from repository`() = runTest {
        val expected = Portfolio(
            assets = listOf(
                Asset(
                    coinId = "bitcoin",
                    coinName = "Bitcoin",
                    coinSymbol = "BTC",
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
        val result = useCase(Currency.USD).first()

        assertEquals(1, result.assets.size)
        assertEquals("bitcoin", result.assets.first().coinId)
        assertEquals(82500.0, result.totalCurrentValue)
    }

    @Test
    fun `returns empty portfolio when no assets`() = runTest {
        val repository = object : PortfolioRepository {
            override fun getPortfolio(currency: Currency): Flow<Portfolio> = flowOf(Portfolio())
            override suspend fun refreshPrices(currency: Currency): Result<Unit> = Result.success(Unit)
        }

        val useCase = GetPortfolioUseCase(repository)
        val result = useCase(Currency.USD).first()

        assertEquals(0, result.assets.size)
        assertEquals(0.0, result.totalCurrentValue)
    }
}
