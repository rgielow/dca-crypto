package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.repository.TransactionRepository

class DeleteTransactionUseCase(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(id: Long): Result<Unit> = runCatching {
        repository.deleteTransaction(id)
    }
}
