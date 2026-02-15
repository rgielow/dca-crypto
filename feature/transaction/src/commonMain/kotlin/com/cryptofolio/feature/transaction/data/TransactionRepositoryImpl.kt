package com.cryptofolio.feature.transaction.data

import com.cryptofolio.core.database.dao.TransactionDao
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import com.cryptofolio.feature.transaction.data.mapper.toDomain
import com.cryptofolio.feature.transaction.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCoinId(coinId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getTransactionById(id: Long): Transaction? =
        transactionDao.getTransactionById(id)?.toDomain()

    override suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertTransaction(transaction.toEntity())

    override suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction.toEntity())

    override suspend fun deleteTransaction(id: Long) =
        transactionDao.deleteTransaction(id)

    override suspend fun deleteAllTransactions() =
        transactionDao.deleteAllTransactions()
}
