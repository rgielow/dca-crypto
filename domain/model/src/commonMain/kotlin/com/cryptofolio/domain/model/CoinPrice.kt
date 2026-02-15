package com.cryptofolio.domain.model

data class CoinPrice(
    val coinId: String,
    val price: Double,
    val priceChangePercentage24h: Double = 0.0,
    val currency: Currency = Currency.USD,
)
