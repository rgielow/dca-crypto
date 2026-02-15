package com.cryptofolio.feature.portfolio.data

import com.cryptofolio.core.network.api.CoinGeckoApi
import com.cryptofolio.core.network.dto.CoinPriceDetailDto
import com.cryptofolio.domain.model.CoinMarket
import com.cryptofolio.domain.model.CoinPrice
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.repository.CoinPriceRepository

class CoinPriceRepositoryImpl(
    private val api: CoinGeckoApi,
) : CoinPriceRepository {

    override suspend fun getCoinPrices(coinIds: List<String>, currency: Currency): Result<List<CoinPrice>> = runCatching {
        if (coinIds.isEmpty()) return@runCatching emptyList()
        val response = api.getSimplePrice(
            ids = coinIds.joinToString(","),
            vsCurrencies = currency.code,
        )
        response.map { (coinId, detail) ->
            CoinPrice(
                coinId = coinId,
                price = detail.getPriceByCurrency(currency),
                priceChangePercentage24h = detail.getChangeByCurrency(currency),
                currency = currency,
            )
        }
    }

    override suspend fun searchCoins(query: String): Result<List<CoinMarket>> = runCatching {
        val response = api.searchCoins(query)
        response.coins.map { dto ->
            CoinMarket(
                id = dto.id,
                symbol = dto.symbol,
                name = dto.name,
                image = dto.thumb ?: "",
                marketCapRank = dto.marketCapRank ?: 0,
            )
        }
    }

    override suspend fun getTopCoins(currency: Currency, limit: Int): Result<List<CoinMarket>> = runCatching {
        api.getCoinsMarkets(
            vsCurrency = currency.code,
            perPage = limit,
        ).map { dto ->
            CoinMarket(
                id = dto.id,
                symbol = dto.symbol,
                name = dto.name,
                image = dto.image ?: "",
                currentPrice = dto.currentPrice ?: 0.0,
                marketCap = dto.marketCap ?: 0,
                marketCapRank = dto.marketCapRank ?: 0,
                priceChangePercentage24h = dto.priceChangePercentage24h ?: 0.0,
            )
        }
    }

    private fun CoinPriceDetailDto.getPriceByCurrency(currency: Currency): Double = when (currency) {
        Currency.USD -> usd ?: 0.0
        Currency.BRL -> brl ?: 0.0
        Currency.EUR -> eur ?: 0.0
    }

    private fun CoinPriceDetailDto.getChangeByCurrency(currency: Currency): Double = when (currency) {
        Currency.USD -> usd_24h_change ?: 0.0
        Currency.BRL -> brl_24h_change ?: 0.0
        Currency.EUR -> eur_24h_change ?: 0.0
    }
}
