package com.cryptofolio.feature.transaction.detail

import com.cryptofolio.domain.model.Transaction

data class TransactionDetailUiState(
    val transaction: Transaction? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)

sealed interface TransactionDetailAction {
    data object Delete : TransactionDetailAction
    data object NavigateBack : TransactionDetailAction
}

sealed interface TransactionDetailEvent {
    data object NavigateBack : TransactionDetailEvent
    data class ShowError(val message: String) : TransactionDetailEvent
}
