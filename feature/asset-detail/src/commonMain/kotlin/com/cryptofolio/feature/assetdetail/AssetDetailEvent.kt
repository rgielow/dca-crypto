package com.cryptofolio.feature.assetdetail

sealed interface AssetDetailEvent {
    data object NavigateBack : AssetDetailEvent
    data class NavigateToTransaction(val id: Long) : AssetDetailEvent
}
