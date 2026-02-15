package com.cryptofolio.domain.repository

import com.cryptofolio.domain.model.CoinMarket
import com.cryptofolio.domain.model.CoinPrice
import com.cryptofolio.domain.model.Currency

interface CoinPriceRepository {
    suspend fun getCoinPrices(coinIds: List<String>, currency: Currency): Result<List<CoinPrice>>
    suspend fun searchCoins(query: String): Result<List<CoinMarket>>
    suspend fun getTopCoins(currency: Currency, limit: Int = 100): Result<List<CoinMarket>>
}
