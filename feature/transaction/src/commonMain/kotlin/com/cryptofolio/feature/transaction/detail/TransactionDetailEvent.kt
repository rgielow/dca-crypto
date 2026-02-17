package com.cryptofolio.feature.transaction.detail

sealed interface TransactionDetailEvent {
    data object NavigateBack : TransactionDetailEvent
    data class ShowError(val message: String) : TransactionDetailEvent
}
