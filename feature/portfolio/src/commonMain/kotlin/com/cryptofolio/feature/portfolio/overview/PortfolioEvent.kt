package com.cryptofolio.feature.portfolio.overview

sealed interface PortfolioEvent {
    data class NavigateToAssetDetail(val coinId: String) : PortfolioEvent
    data class ShowError(val message: String) : PortfolioEvent
}
