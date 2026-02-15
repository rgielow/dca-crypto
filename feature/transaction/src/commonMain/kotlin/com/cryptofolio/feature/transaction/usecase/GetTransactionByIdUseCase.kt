package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository

class GetTransactionByIdUseCase(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(id: Long): Result<Transaction> = runCatching {
        repository.getTransactionById(id) ?: error("Transaction not found")
    }
}
