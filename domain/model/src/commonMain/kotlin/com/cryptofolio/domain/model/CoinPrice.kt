package com.cryptofolio.domain.model

data class CoinPrice(
    val coinId: String,
    val price: Double,
    val priceChangePercentage24h: Double = 0.0,
    val currency: Currency = Currency.USD,
) {
    companion object {
        fun mock(
            coinId: String = "bitcoin",
            price: Double = 60000.0,
            priceChangePercentage24h: Double = 2.5,
            currency: Currency = Currency.USD,
        ) = CoinPrice(
            coinId = coinId,
            price = price,
            priceChangePercentage24h = priceChangePercentage24h,
            currency = currency,
        )
    }
}
