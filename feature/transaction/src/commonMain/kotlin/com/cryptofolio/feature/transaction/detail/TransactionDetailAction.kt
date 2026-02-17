package com.cryptofolio.feature.transaction.detail

sealed interface TransactionDetailAction {
    data object Delete : TransactionDetailAction
    data object NavigateBack : TransactionDetailAction
}
