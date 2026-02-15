package com.cryptofolio.domain.model

data class CoinMarket(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String = "",
    val currentPrice: Double = 0.0,
    val marketCap: Long = 0,
    val marketCapRank: Int = 0,
    val priceChangePercentage24h: Double = 0.0,
)
