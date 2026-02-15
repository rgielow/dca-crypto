package com.cryptofolio.feature.portfolio.overview

import androidx.lifecycle.viewModelScope
import com.cryptofolio.core.ui.BaseViewModel
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.feature.portfolio.usecase.GetPortfolioUseCase
import com.cryptofolio.feature.portfolio.usecase.RefreshPricesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val getPortfolio: GetPortfolioUseCase,
    private val refreshPrices: RefreshPricesUseCase,
) : BaseViewModel<PortfolioUiState, PortfolioAction, PortfolioEvent>(
    PortfolioUiState()
) {

    init {
        onAction(PortfolioAction.LoadPortfolio)
        onAction(PortfolioAction.RefreshPrices)
    }

    override fun onAction(action: PortfolioAction) {
        when (action) {
            PortfolioAction.LoadPortfolio -> loadPortfolio()
            PortfolioAction.RefreshPrices -> refresh()
            is PortfolioAction.SelectAsset -> sendEvent(PortfolioEvent.NavigateToAssetDetail(action.coinId))
        }
    }

    private fun loadPortfolio() {
        getPortfolio(Currency.USD)
            .onStart { updateState { copy(isLoading = true) } }
            .onEach { portfolio ->
                updateState { copy(portfolio = portfolio, isLoading = false, error = null) }
            }
            .catch { e ->
                updateState { copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    private fun refresh() {
        viewModelScope.launch {
            updateState { copy(isRefreshing = true) }
            refreshPrices(Currency.USD)
                .onFailure { sendEvent(PortfolioEvent.ShowError(it.message ?: "Error refreshing prices")) }
            updateState { copy(isRefreshing = false) }
        }
    }
}
