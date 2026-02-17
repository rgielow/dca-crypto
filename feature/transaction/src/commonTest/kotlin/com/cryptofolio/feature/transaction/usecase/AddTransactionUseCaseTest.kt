package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class AddTransactionUseCaseTest {

    private val fakeRepository = FakeTransactionRepository()
    private val useCase = AddTransactionUseCase(fakeRepository)

    @Test
    fun `given valid transaction - when invoke - then return Success`() = runTest {
        // Given
        val transaction = Transaction.mock()

        // When
        val result = useCase(transaction)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeRepository.insertedTransactions.isNotEmpty())
    }

    @Test
    fun `given zero amount - when invoke - then return Failure`() = runTest {
        // Given
        val transaction = Transaction.mock(amount = 0.0)

        // When
        val result = useCase(transaction)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `given negative amount - when invoke - then return Failure`() = runTest {
        // Given
        val transaction = Transaction.mock(amount = -1.0)

        // When
        val result = useCase(transaction)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `given zero price - when invoke - then return Failure`() = runTest {
        // Given
        val transaction = Transaction.mock(pricePerUnit = 0.0)

        // When
        val result = useCase(transaction)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `given blank coinId - when invoke - then return Failure`() = runTest {
        // Given
        val transaction = Transaction.mock(coinId = "")

        // When
        val result = useCase(transaction)

        // Then
        assertTrue(result.isFailure)
    }
}

private class FakeTransactionRepository : TransactionRepository {
    val insertedTransactions = mutableListOf<Transaction>()

    override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(emptyList())
    override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> = flowOf(emptyList())
    override suspend fun getTransactionById(id: Long): Transaction? = null
    override suspend fun insertTransaction(transaction: Transaction) {
        insertedTransactions.add(transaction)
    }
    override suspend fun updateTransaction(transaction: Transaction) {}
    override suspend fun deleteTransaction(id: Long) {}
    override suspend fun deleteAllTransactions() {}
}
