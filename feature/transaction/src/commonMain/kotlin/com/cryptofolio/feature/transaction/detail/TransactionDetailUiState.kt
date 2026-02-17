package com.cryptofolio.feature.transaction.detail

import com.cryptofolio.domain.model.Transaction

data class TransactionDetailUiState(
    val transaction: Transaction? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)
