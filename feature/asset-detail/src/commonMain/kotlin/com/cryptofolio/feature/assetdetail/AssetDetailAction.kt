package com.cryptofolio.feature.assetdetail

sealed interface AssetDetailAction {
    data object NavigateBack : AssetDetailAction
    data class SelectTransaction(val id: Long) : AssetDetailAction
}
