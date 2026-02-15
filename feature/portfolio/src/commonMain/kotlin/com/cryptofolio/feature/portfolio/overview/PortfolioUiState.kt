package com.cryptofolio.feature.portfolio.overview

import com.cryptofolio.domain.model.Portfolio

data class PortfolioUiState(
    val portfolio: Portfolio = Portfolio(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)

sealed interface PortfolioAction {
    data object LoadPortfolio : PortfolioAction
    data object RefreshPrices : PortfolioAction
    data class SelectAsset(val coinId: String) : PortfolioAction
}

sealed interface PortfolioEvent {
    data class NavigateToAssetDetail(val coinId: String) : PortfolioEvent
    data class ShowError(val message: String) : PortfolioEvent
}
