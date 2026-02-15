package com.cryptofolio.core.network.api

import com.cryptofolio.core.network.ApiConstants
import com.cryptofolio.core.network.dto.CoinMarketDto
import com.cryptofolio.core.network.dto.CoinPriceResponse
import com.cryptofolio.core.network.dto.CoinSearchResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CoinGeckoApiImpl(
    private val client: HttpClient,
) : CoinGeckoApi {

    override suspend fun getCoinsMarkets(
        vsCurrency: String,
        perPage: Int,
        page: Int,
    ): List<CoinMarketDto> {
        return client.get(ApiConstants.COINS_MARKETS) {
            parameter("vs_currency", vsCurrency)
            parameter("order", "market_cap_desc")
            parameter("per_page", perPage)
            parameter("page", page)
            parameter("sparkline", false)
        }.body()
    }

    override suspend fun getSimplePrice(
        ids: String,
        vsCurrencies: String,
        includeChangePercentage: Boolean,
    ): CoinPriceResponse {
        return client.get(ApiConstants.SIMPLE_PRICE) {
            parameter("ids", ids)
            parameter("vs_currencies", vsCurrencies)
            if (includeChangePercentage) {
                parameter("include_24hr_change", true)
            }
        }.body()
    }

    override suspend fun searchCoins(query: String): CoinSearchResponseDto {
        return client.get(ApiConstants.SEARCH) {
            parameter("query", query)
        }.body()
    }
}
