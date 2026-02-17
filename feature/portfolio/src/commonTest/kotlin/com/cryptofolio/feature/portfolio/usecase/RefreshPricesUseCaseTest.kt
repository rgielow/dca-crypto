package com.cryptofolio.feature.portfolio.usecase

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RefreshPricesUseCaseTest {

    @Test
    fun `given successful refresh - when invoke - then return Success`() = runTest {
        // Given
        val repository = FakePortfolioRepository(refreshResult = Result.success(Unit))
        val useCase = RefreshPricesUseCase(repository)

        // When
        val result = useCase(Currency.USD)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `given failed refresh - when invoke - then return Failure`() = runTest {
        // Given
        val error = Exception("Network error")
        val repository = FakePortfolioRepository(refreshResult = Result.failure(error))
        val useCase = RefreshPricesUseCase(repository)

        // When
        val result = useCase(Currency.USD)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `given BRL currency - when invoke - then pass currency to repository`() = runTest {
        // Given
        var receivedCurrency: Currency? = null
        val repository = object : PortfolioRepository {
            override fun getPortfolio(currency: Currency): Flow<Portfolio> = flowOf(Portfolio())
            override suspend fun refreshPrices(currency: Currency): Result<Unit> {
                receivedCurrency = currency
                return Result.success(Unit)
            }
        }
        val useCase = RefreshPricesUseCase(repository)

        // When
        useCase(Currency.BRL)

        // Then
        assertEquals(Currency.BRL, receivedCurrency)
    }
}

private class FakePortfolioRepository(
    private val refreshResult: Result<Unit> = Result.success(Unit),
) : PortfolioRepository {
    override fun getPortfolio(currency: Currency): Flow<Portfolio> = flowOf(Portfolio())
    override suspend fun refreshPrices(currency: Currency): Result<Unit> = refreshResult
}
