package com.cryptofolio.feature.assetdetail

import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Transaction

data class AssetDetailUiState(
    val asset: Asset? = null,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

sealed interface AssetDetailAction {
    data object NavigateBack : AssetDetailAction
    data class SelectTransaction(val id: Long) : AssetDetailAction
}

sealed interface AssetDetailEvent {
    data object NavigateBack : AssetDetailEvent
    data class NavigateToTransaction(val id: Long) : AssetDetailEvent
}
