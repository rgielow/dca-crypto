package com.cryptofolio.feature.transaction.add

import androidx.lifecycle.viewModelScope
import com.cryptofolio.core.common.DateTimeUtil
import com.cryptofolio.core.ui.BaseViewModel
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.feature.transaction.usecase.AddTransactionUseCase
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val addTransaction: AddTransactionUseCase,
) : BaseViewModel<AddTransactionUiState, AddTransactionAction, AddTransactionEvent>(
    AddTransactionUiState()
) {

    override fun onAction(action: AddTransactionAction) {
        when (action) {
            is AddTransactionAction.UpdateCoinId -> updateState { copy(coinId = action.coinId, coinError = null) }
            is AddTransactionAction.UpdateCoinName -> updateState { copy(coinName = action.name) }
            is AddTransactionAction.UpdateCoinSymbol -> updateState { copy(coinSymbol = action.symbol) }
            is AddTransactionAction.UpdateType -> updateState { copy(type = action.type) }
            is AddTransactionAction.UpdateAmount -> updateState { copy(amount = action.amount, amountError = null) }
            is AddTransactionAction.UpdatePrice -> updateState { copy(pricePerUnit = action.price, priceError = null) }
            is AddTransactionAction.UpdateFee -> updateState { copy(fee = action.fee) }
            is AddTransactionAction.UpdateExchange -> updateState { copy(exchange = action.exchange) }
            is AddTransactionAction.UpdateCurrency -> updateState { copy(currency = action.currency) }
            is AddTransactionAction.UpdateNotes -> updateState { copy(notes = action.notes) }
            AddTransactionAction.Save -> save()
        }
    }

    private fun save() {
        val state = currentState
        var hasError = false

        if (state.coinId.isBlank()) {
            updateState { copy(coinError = "Coin is required") }
            hasError = true
        }
        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            updateState { copy(amountError = "Valid amount required") }
            hasError = true
        }
        val price = state.pricePerUnit.toDoubleOrNull()
        if (price == null || price <= 0) {
            updateState { copy(priceError = "Valid price required") }
            hasError = true
        }
        if (hasError) return

        val transaction = Transaction(
            coinId = state.coinId,
            coinName = state.coinName,
            coinSymbol = state.coinSymbol.uppercase(),
            type = state.type,
            amount = amount!!,
            pricePerUnit = price!!,
            fee = state.fee.toDoubleOrNull() ?: 0.0,
            exchange = state.exchange,
            currency = state.currency,
            date = DateTimeUtil.now(),
            notes = state.notes,
        )

        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            addTransaction(transaction)
                .onSuccess { sendEvent(AddTransactionEvent.TransactionSaved) }
                .onFailure { sendEvent(AddTransactionEvent.ShowError(it.message ?: "Error")) }
            updateState { copy(isLoading = false) }
        }
    }
}
