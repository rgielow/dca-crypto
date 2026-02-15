package com.cryptofolio.feature.transaction.list

import androidx.lifecycle.viewModelScope
import com.cryptofolio.core.ui.BaseViewModel
import com.cryptofolio.feature.transaction.usecase.DeleteTransactionUseCase
import com.cryptofolio.feature.transaction.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val getTransactions: GetTransactionsUseCase,
    private val deleteTransaction: DeleteTransactionUseCase,
) : BaseViewModel<TransactionListUiState, TransactionListAction, TransactionListEvent>(
    TransactionListUiState()
) {

    init {
        onAction(TransactionListAction.LoadTransactions)
    }

    override fun onAction(action: TransactionListAction) {
        when (action) {
            TransactionListAction.LoadTransactions -> loadTransactions()
            is TransactionListAction.DeleteTransaction -> delete(action.id)
            TransactionListAction.AddTransaction -> sendEvent(TransactionListEvent.NavigateToAddTransaction)
            is TransactionListAction.SelectTransaction -> sendEvent(TransactionListEvent.NavigateToTransactionDetail(action.id))
        }
    }

    private fun loadTransactions() {
        getTransactions()
            .onStart { updateState { copy(isLoading = true) } }
            .onEach { transactions ->
                updateState { copy(transactions = transactions, isLoading = false, error = null) }
            }
            .catch { e ->
                updateState { copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            deleteTransaction(id)
                .onSuccess { sendEvent(TransactionListEvent.TransactionDeleted) }
                .onFailure { sendEvent(TransactionListEvent.ShowError(it.message ?: "Error")) }
        }
    }
}
