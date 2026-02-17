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
) {
    companion object {
        fun mock(
            id: String = "bitcoin",
            symbol: String = "btc",
            name: String = "Bitcoin",
            image: String = "",
            currentPrice: Double = 60000.0,
            marketCap: Long = 1_000_000_000,
            marketCapRank: Int = 1,
            priceChangePercentage24h: Double = 2.5,
        ) = CoinMarket(
            id = id,
            symbol = symbol,
            name = name,
            image = image,
            currentPrice = currentPrice,
            marketCap = marketCap,
            marketCapRank = marketCapRank,
            priceChangePercentage24h = priceChangePercentage24h,
        )
    }
}
