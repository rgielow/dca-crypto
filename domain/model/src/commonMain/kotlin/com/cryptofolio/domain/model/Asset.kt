package com.cryptofolio.domain.model

data class Asset(
    val coinId: String,
    val coinName: String,
    val coinSymbol: String,
    val totalAmount: Double,
    val averageBuyPrice: Double,
    val totalInvested: Double,
    val currentPrice: Double = 0.0,
    val currentValue: Double = totalAmount * currentPrice,
    val profitLoss: Double = currentValue - totalInvested,
    val profitLossPercentage: Double = if (totalInvested > 0) (profitLoss / totalInvested) * 100 else 0.0,
    val currency: Currency = Currency.USD,
) {
    companion object {
        fun mock(
            coinId: String = "bitcoin",
            coinName: String = "Bitcoin",
            coinSymbol: String = "BTC",
            totalAmount: Double = 1.0,
            averageBuyPrice: Double = 50000.0,
            totalInvested: Double = 50000.0,
            currentPrice: Double = 60000.0,
            currentValue: Double = totalAmount * currentPrice,
            profitLoss: Double = currentValue - totalInvested,
            profitLossPercentage: Double = if (totalInvested > 0) (profitLoss / totalInvested) * 100 else 0.0,
            currency: Currency = Currency.USD,
        ) = Asset(
            coinId = coinId,
            coinName = coinName,
            coinSymbol = coinSymbol,
            totalAmount = totalAmount,
            averageBuyPrice = averageBuyPrice,
            totalInvested = totalInvested,
            currentPrice = currentPrice,
            currentValue = currentValue,
            profitLoss = profitLoss,
            profitLossPercentage = profitLossPercentage,
            currency = currency,
        )
    }
}
