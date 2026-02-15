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
)
