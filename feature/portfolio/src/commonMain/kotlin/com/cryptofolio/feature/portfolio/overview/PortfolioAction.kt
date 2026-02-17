package com.cryptofolio.feature.portfolio.overview

sealed interface PortfolioAction {
    data object LoadPortfolio : PortfolioAction
    data object RefreshPrices : PortfolioAction
    data class SelectAsset(val coinId: String) : PortfolioAction
}
