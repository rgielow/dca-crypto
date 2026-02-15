package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(
    private val repository: TransactionRepository,
) {
    operator fun invoke(): Flow<List<Transaction>> = repository.getAllTransactions()

    operator fun invoke(coinId: String): Flow<List<Transaction>> =
        repository.getTransactionsByCoinId(coinId)
}
