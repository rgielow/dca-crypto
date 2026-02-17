package com.cryptofolio.feature.portfolio.data

import com.cryptofolio.core.network.api.CoinGeckoApi
import com.cryptofolio.core.network.dto.CoinMarketDto
import com.cryptofolio.core.network.dto.CoinPriceDetailDto
import com.cryptofolio.core.network.dto.CoinPriceResponse
import com.cryptofolio.core.network.dto.CoinSearchDto
import com.cryptofolio.core.network.dto.CoinSearchResponseDto
import com.cryptofolio.domain.model.Currency
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoinPriceRepositoryImplTest {

    @Test
    fun `given API returns prices - when getCoinPrices - then return mapped CoinPrice list`() =
        runTest {
            // Given
            val apiResponse = mapOf(
                "bitcoin" to CoinPriceDetailDto(usd = 60000.0, usd_24h_change = 2.5),
            )
            val repository = CoinPriceRepositoryImpl(FakeCoinGeckoApi(priceResponse = apiResponse))

            // When
            val result = repository.getCoinPrices(listOf("bitcoin"), Currency.USD)

            // Then
            assertTrue(result.isSuccess)
            val prices = result.getOrThrow()
            assertEquals(1, prices.size)
            assertEquals("bitcoin", prices.first().coinId)
            assertEquals(60000.0, prices.first().price)
            assertEquals(2.5, prices.first().priceChangePercentage24h)
        }

    @Test
    fun `given empty coin list - when getCoinPrices - then return empty list`() = runTest {
        // Given
        val repository = CoinPriceRepositoryImpl(FakeCoinGeckoApi())

        // When
        val result = repository.getCoinPrices(emptyList(), Currency.USD)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(emptyList(), result.getOrThrow())
    }

    @Test
    fun `given API fails - when getCoinPrices - then return Failure`() = runTest {
        // Given
        val repository = CoinPriceRepositoryImpl(FakeCoinGeckoApi(shouldFail = true))

        // When
        val result = repository.getCoinPrices(listOf("bitcoin"), Currency.USD)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `given API returns search results - when searchCoins - then return mapped CoinMarket list`() =
        runTest {
            // Given
            val searchResponse = CoinSearchResponseDto(
                coins = listOf(
                    CoinSearchDto(
                        id = "bitcoin",
                        name = "Bitcoin",
                        symbol = "btc",
                        marketCapRank = 1,
                        thumb = "https://example.com/btc.png",
                    ),
                ),
            )
            val repository = CoinPriceRepositoryImpl(FakeCoinGeckoApi(searchResponse = searchResponse))

            // When
            val result = repository.searchCoins("bit")

            // Then
            assertTrue(result.isSuccess)
            val coins = result.getOrThrow()
            assertEquals(1, coins.size)
            assertEquals("bitcoin", coins.first().id)
            assertEquals("Bitcoin", coins.first().name)
        }
}

private class FakeCoinGeckoApi(
    private val priceResponse: CoinPriceResponse = emptyMap(),
    private val searchResponse: CoinSearchResponseDto = CoinSearchResponseDto(emptyList()),
    private val shouldFail: Boolean = false,
) : CoinGeckoApi {
    override suspend fun getCoinsMarkets(
        vsCurrency: String,
        perPage: Int,
        page: Int,
    ): List<CoinMarketDto> = emptyList()

    override suspend fun getSimplePrice(
        ids: String,
        vsCurrencies: String,
        includeChangePercentage: Boolean,
    ): CoinPriceResponse {
        if (shouldFail) throw Exception("API error")
        return priceResponse
    }

    override suspend fun searchCoins(query: String): CoinSearchResponseDto = searchResponse
}
