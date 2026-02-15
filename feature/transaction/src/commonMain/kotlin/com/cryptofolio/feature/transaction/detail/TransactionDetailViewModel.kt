package com.cryptofolio.feature.transaction.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cryptofolio.core.ui.BaseViewModel
import com.cryptofolio.feature.transaction.usecase.DeleteTransactionUseCase
import com.cryptofolio.feature.transaction.usecase.GetTransactionByIdUseCase
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getTransactionById: GetTransactionByIdUseCase,
    private val deleteTransaction: DeleteTransactionUseCase,
) : BaseViewModel<TransactionDetailUiState, TransactionDetailAction, TransactionDetailEvent>(
    TransactionDetailUiState()
) {

    private val transactionId: Long = checkNotNull(savedStateHandle["transactionId"])

    init {
        loadTransaction()
    }

    override fun onAction(action: TransactionDetailAction) {
        when (action) {
            TransactionDetailAction.Delete -> delete()
            TransactionDetailAction.NavigateBack -> sendEvent(TransactionDetailEvent.NavigateBack)
        }
    }

    private fun loadTransaction() {
        viewModelScope.launch {
            getTransactionById(transactionId)
                .onSuccess { updateState { copy(transaction = it, isLoading = false) } }
                .onFailure { updateState { copy(error = it.message, isLoading = false) } }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            deleteTransaction(transactionId)
                .onSuccess { sendEvent(TransactionDetailEvent.NavigateBack) }
                .onFailure { sendEvent(TransactionDetailEvent.ShowError(it.message ?: "Error")) }
        }
    }
}
