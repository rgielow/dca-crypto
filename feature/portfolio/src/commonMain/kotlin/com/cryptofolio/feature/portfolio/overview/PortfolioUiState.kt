package com.cryptofolio.feature.portfolio.overview

import com.cryptofolio.domain.model.Portfolio

data class PortfolioUiState(
    val portfolio: Portfolio = Portfolio(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
)
