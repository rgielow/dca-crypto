package com.cryptofolio.feature.transaction.list

import com.cryptofolio.domain.model.Transaction

data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

sealed interface TransactionListAction {
    data object LoadTransactions : TransactionListAction
    data class DeleteTransaction(val id: Long) : TransactionListAction
    data object AddTransaction : TransactionListAction
    data class SelectTransaction(val id: Long) : TransactionListAction
}

sealed interface TransactionListEvent {
    data object NavigateToAddTransaction : TransactionListEvent
    data class NavigateToTransactionDetail(val id: Long) : TransactionListEvent
    data class ShowError(val message: String) : TransactionListEvent
    data object TransactionDeleted : TransactionListEvent
}
