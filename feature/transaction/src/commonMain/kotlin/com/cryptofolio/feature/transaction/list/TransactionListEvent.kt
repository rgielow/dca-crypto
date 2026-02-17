package com.cryptofolio.feature.transaction.list

sealed interface TransactionListEvent {
    data object NavigateToAddTransaction : TransactionListEvent
    data class NavigateToTransactionDetail(val id: Long) : TransactionListEvent
    data class ShowError(val message: String) : TransactionListEvent
    data object TransactionDeleted : TransactionListEvent
}
