package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetTransactionByIdUseCaseTest {

    @Test
    fun `given transaction exists - when invoke - then return Success with transaction`() = runTest {
        // Given
        val expected = Transaction.mock(id = 1)
        val repository = FakeGetByIdTransactionRepository(transaction = expected)
        val useCase = GetTransactionByIdUseCase(repository)

        // When
        val result = useCase(1L)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `given transaction not found - when invoke - then return Failure`() = runTest {
        // Given
        val repository = FakeGetByIdTransactionRepository(transaction = null)
        val useCase = GetTransactionByIdUseCase(repository)

        // When
        val result = useCase(999L)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Transaction not found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `given repository throws exception - when invoke - then return Failure`() = runTest {
        // Given
        val repository = FakeGetByIdTransactionRepository(shouldFail = true)
        val useCase = GetTransactionByIdUseCase(repository)

        // When
        val result = useCase(1L)

        // Then
        assertTrue(result.isFailure)
    }
}

private class FakeGetByIdTransactionRepository(
    private val transaction: Transaction? = null,
    private val shouldFail: Boolean = false,
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(emptyList())
    override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> = flowOf(emptyList())
    override suspend fun getTransactionById(id: Long): Transaction? {
        if (shouldFail) throw Exception("Database error")
        return transaction
    }
    override suspend fun insertTransaction(transaction: Transaction) {}
    override suspend fun updateTransaction(transaction: Transaction) {}
    override suspend fun deleteTransaction(id: Long) {}
    override suspend fun deleteAllTransactions() {}
}
