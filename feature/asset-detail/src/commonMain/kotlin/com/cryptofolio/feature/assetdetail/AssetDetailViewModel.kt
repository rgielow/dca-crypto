package com.cryptofolio.feature.assetdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cryptofolio.core.ui.BaseViewModel
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.repository.AssetRepository
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class AssetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val assetRepository: AssetRepository,
    private val transactionRepository: TransactionRepository,
) : BaseViewModel<AssetDetailUiState, AssetDetailAction, AssetDetailEvent>(
    AssetDetailUiState()
) {

    private val coinId: String = checkNotNull(savedStateHandle["coinId"])

    init {
        loadAssetDetail()
    }

    override fun onAction(action: AssetDetailAction) {
        when (action) {
            AssetDetailAction.NavigateBack -> sendEvent(AssetDetailEvent.NavigateBack)
            is AssetDetailAction.SelectTransaction -> sendEvent(AssetDetailEvent.NavigateToTransaction(action.id))
        }
    }

    private fun loadAssetDetail() {
        combine(
            assetRepository.getAssetByCoinId(coinId, Currency.USD),
            transactionRepository.getTransactionsByCoinId(coinId),
        ) { asset, transactions ->
            AssetDetailUiState(
                asset = asset,
                transactions = transactions,
                isLoading = false,
            )
        }
            .onStart { updateState { copy(isLoading = true) } }
            .onEach { state -> updateState { state } }
            .catch { e -> updateState { copy(isLoading = false, error = e.message) } }
            .launchIn(viewModelScope)
    }
}
