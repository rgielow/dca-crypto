package com.cryptofolio.domain.model

data class Portfolio(
    val assets: List<Asset> = emptyList(),
    val totalInvested: Double = assets.sumOf { it.totalInvested },
    val totalCurrentValue: Double = assets.sumOf { it.currentValue },
    val totalProfitLoss: Double = totalCurrentValue - totalInvested,
    val totalProfitLossPercentage: Double = if (totalInvested > 0) (totalProfitLoss / totalInvested) * 100 else 0.0,
    val currency: Currency = Currency.USD,
) {
    companion object {
        fun mock(
            assets: List<Asset> = listOf(Asset.mock()),
            totalInvested: Double = assets.sumOf { it.totalInvested },
            totalCurrentValue: Double = assets.sumOf { it.currentValue },
            totalProfitLoss: Double = totalCurrentValue - totalInvested,
            totalProfitLossPercentage: Double = if (totalInvested > 0) (totalProfitLoss / totalInvested) * 100 else 0.0,
            currency: Currency = Currency.USD,
        ) = Portfolio(
            assets = assets,
            totalInvested = totalInvested,
            totalCurrentValue = totalCurrentValue,
            totalProfitLoss = totalProfitLoss,
            totalProfitLossPercentage = totalProfitLossPercentage,
            currency = currency,
        )
    }
}
