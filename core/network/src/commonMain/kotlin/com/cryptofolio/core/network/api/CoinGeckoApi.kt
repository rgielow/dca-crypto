package com.cryptofolio.core.network.api

import com.cryptofolio.core.network.dto.CoinMarketDto
import com.cryptofolio.core.network.dto.CoinPriceResponse
import com.cryptofolio.core.network.dto.CoinSearchResponseDto

interface CoinGeckoApi {
    suspend fun getCoinsMarkets(
        vsCurrency: String,
        perPage: Int = 100,
        page: Int = 1,
    ): List<CoinMarketDto>

    suspend fun getSimplePrice(
        ids: String,
        vsCurrencies: String,
        includeChangePercentage: Boolean = true,
    ): CoinPriceResponse

    suspend fun searchCoins(query: String): CoinSearchResponseDto
}
