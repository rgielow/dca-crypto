package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeleteTransactionUseCaseTest {

    @Test
    fun `given valid id - when invoke - then return Success`() = runTest {
        // Given
        val repository = FakeDeleteTransactionRepository()
        val useCase = DeleteTransactionUseCase(repository)

        // When
        val result = useCase(1L)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1L, repository.deletedId)
    }

    @Test
    fun `given repository throws exception - when invoke - then return Failure`() = runTest {
        // Given
        val repository = FakeDeleteTransactionRepository(shouldFail = true)
        val useCase = DeleteTransactionUseCase(repository)

        // When
        val result = useCase(1L)

        // Then
        assertTrue(result.isFailure)
    }
}

private class FakeDeleteTransactionRepository(
    private val shouldFail: Boolean = false,
) : TransactionRepository {
    var deletedId: Long? = null

    override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(emptyList())
    override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> = flowOf(emptyList())
    override suspend fun getTransactionById(id: Long): Transaction? = null
    override suspend fun insertTransaction(transaction: Transaction) {}
    override suspend fun updateTransaction(transaction: Transaction) {}
    override suspend fun deleteTransaction(id: Long) {
        if (shouldFail) throw Exception("Delete failed")
        deletedId = id
    }
    override suspend fun deleteAllTransactions() {}
}
