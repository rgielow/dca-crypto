package com.cryptofolio.feature.transaction.list

import com.cryptofolio.domain.model.Transaction

data class TransactionListUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)
