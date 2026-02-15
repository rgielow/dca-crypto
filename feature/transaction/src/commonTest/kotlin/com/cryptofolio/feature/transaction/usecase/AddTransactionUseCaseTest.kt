package com.cryptofolio.feature.transaction.usecase

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Exchange
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertTrue

class AddTransactionUseCaseTest {

    private val fakeRepository = FakeTransactionRepository()
    private val useCase = AddTransactionUseCase(fakeRepository)
    private val now = Clock.System.now()

    @Test
    fun `successfully adds valid transaction`() = runTest {
        val transaction = createTransaction(amount = 1.0, pricePerUnit = 50000.0)
        val result = useCase(transaction)
        assertTrue(result.isSuccess)
        assertTrue(fakeRepository.insertedTransactions.isNotEmpty())
    }

    @Test
    fun `fails when amount is zero`() = runTest {
        val transaction = createTransaction(amount = 0.0, pricePerUnit = 50000.0)
        val result = useCase(transaction)
        assertTrue(result.isFailure)
    }

    @Test
    fun `fails when amount is negative`() = runTest {
        val transaction = createTransaction(amount = -1.0, pricePerUnit = 50000.0)
        val result = useCase(transaction)
        assertTrue(result.isFailure)
    }

    @Test
    fun `fails when price is zero`() = runTest {
        val transaction = createTransaction(amount = 1.0, pricePerUnit = 0.0)
        val result = useCase(transaction)
        assertTrue(result.isFailure)
    }

    @Test
    fun `fails when coinId is blank`() = runTest {
        val transaction = createTransaction(amount = 1.0, pricePerUnit = 50000.0, coinId = "")
        val result = useCase(transaction)
        assertTrue(result.isFailure)
    }

    private fun createTransaction(
        amount: Double,
        pricePerUnit: Double,
        coinId: String = "bitcoin",
    ) = Transaction(
        coinId = coinId,
        coinName = "Bitcoin",
        coinSymbol = "BTC",
        type = TransactionType.BUY,
        amount = amount,
        pricePerUnit = pricePerUnit,
        exchange = Exchange.BINANCE,
        currency = Currency.USD,
        date = now,
    )
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
