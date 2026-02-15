package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository

class AddTransactionUseCase(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> = runCatching {
        require(transaction.amount > 0) { "Amount must be positive" }
        require(transaction.pricePerUnit > 0) { "Price must be positive" }
        require(transaction.coinId.isNotBlank()) { "Coin ID is required" }
        repository.insertTransaction(transaction)
    }
}
