package com.cryptofolio.feature.transaction.list

sealed interface TransactionListAction {
    data object LoadTransactions : TransactionListAction
    data class DeleteTransaction(val id: Long) : TransactionListAction
    data object AddTransaction : TransactionListAction
    data class SelectTransaction(val id: Long) : TransactionListAction
}
