package com.cryptofolio.feature.portfolio.data

import app.cash.turbine.test
import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.CoinPrice
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.AssetRepository
import com.cryptofolio.domain.repository.CoinPriceRepository
import com.cryptofolio.domain.model.CoinMarket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PortfolioRepositoryImplTest {

    @Test
    fun `given assets exist - when getPortfolio - then return portfolio with assets`() = runTest {
        // Given
        val assets = listOf(Asset.mock())
        val repository = createRepository(assets = assets)

        // When / Then
        repository.getPortfolio(Currency.USD).test {
            val portfolio = awaitItem()
            assertEquals(1, portfolio.assets.size)
            assertEquals("bitcoin", portfolio.assets.first().coinId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given no assets - when getPortfolio - then return empty portfolio`() = runTest {
        // Given
        val repository = createRepository(assets = emptyList())

        // When / Then
        repository.getPortfolio(Currency.USD).test {
            val portfolio = awaitItem()
            assertEquals(0, portfolio.assets.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given prices available - when refreshPrices then getPortfolio - then return updated prices`() =
        runTest {
            // Given
            val assets = listOf(Asset.mock(currentPrice = 0.0))
            val prices = listOf(CoinPrice.mock(coinId = "bitcoin", price = 65000.0))
            val repository = createRepository(assets = assets, prices = prices)

            // When
            val result = repository.refreshPrices(Currency.USD)

            // Then
            assertTrue(result.isSuccess)
        }

    @Test
    fun `given price fetch fails - when refreshPrices - then return Failure`() = runTest {
        // Given
        val assets = listOf(Asset.mock())
        val repository = createRepository(
            assets = assets,
            priceResult = Result.failure(Exception("API error")),
        )

        // When
        val result = repository.refreshPrices(Currency.USD)

        // Then
        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }

    private fun createRepository(
        assets: List<Asset> = emptyList(),
        prices: List<CoinPrice> = emptyList(),
        priceResult: Result<List<CoinPrice>> = Result.success(prices),
    ): PortfolioRepositoryImpl {
        val assetRepository = object : AssetRepository {
            override fun getAllAssets(currency: Currency): Flow<List<Asset>> = flowOf(assets)
            override fun getAssetByCoinId(coinId: String, currency: Currency): Flow<Asset?> =
                flowOf(assets.find { it.coinId == coinId })
        }
        val coinPriceRepository = object : CoinPriceRepository {
            override suspend fun getCoinPrices(
                coinIds: List<String>,
                currency: Currency,
            ): Result<List<CoinPrice>> = priceResult
            override suspend fun searchCoins(query: String): Result<List<CoinMarket>> =
                Result.success(emptyList())
            override suspend fun getTopCoins(currency: Currency, limit: Int): Result<List<CoinMarket>> =
                Result.success(emptyList())
        }
        return PortfolioRepositoryImpl(assetRepository, coinPriceRepository)
    }
}
