package com.cryptofolio.feature.transaction.add

sealed interface AddTransactionEvent {
    data object TransactionSaved : AddTransactionEvent
    data class ShowError(val message: String) : AddTransactionEvent
}
