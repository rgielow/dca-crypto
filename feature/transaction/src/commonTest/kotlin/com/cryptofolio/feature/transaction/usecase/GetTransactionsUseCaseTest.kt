package com.cryptofolio.feature.transaction.usecase

import app.cash.turbine.test
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetTransactionsUseCaseTest {

    @Test
    fun `given repository has transactions - when invoke without coinId - then emit all transactions`() =
        runTest {
            // Given
            val transactions = listOf(
                Transaction.mock(id = 1, coinId = "bitcoin"),
                Transaction.mock(id = 2, coinId = "ethereum", coinName = "Ethereum", coinSymbol = "ETH"),
            )
            val repository = FakeGetTransactionsRepository(allTransactions = transactions)
            val useCase = GetTransactionsUseCase(repository)

            // When / Then
            useCase().test {
                assertEquals(transactions, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `given repository has transactions - when invoke with coinId - then emit filtered transactions`() =
        runTest {
            // Given
            val bitcoinTransactions = listOf(Transaction.mock(id = 1, coinId = "bitcoin"))
            val repository = FakeGetTransactionsRepository(coinTransactions = bitcoinTransactions)
            val useCase = GetTransactionsUseCase(repository)

            // When / Then
            useCase("bitcoin").test {
                assertEquals(bitcoinTransactions, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `given empty repository - when invoke - then emit empty list`() = runTest {
        // Given
        val repository = FakeGetTransactionsRepository(allTransactions = emptyList())
        val useCase = GetTransactionsUseCase(repository)

        // When / Then
        useCase().test {
            assertEquals(emptyList(), awaitItem())
            awaitComplete()
        }
    }
}

private class FakeGetTransactionsRepository(
    private val allTransactions: List<Transaction> = emptyList(),
    private val coinTransactions: List<Transaction> = emptyList(),
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(allTransactions)
    override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> = flowOf(coinTransactions)
    override suspend fun getTransactionById(id: Long): Transaction? = null
    override suspend fun insertTransaction(transaction: Transaction) {}
    override suspend fun updateTransaction(transaction: Transaction) {}
    override suspend fun deleteTransaction(id: Long) {}
    override suspend fun deleteAllTransactions() {}
}
