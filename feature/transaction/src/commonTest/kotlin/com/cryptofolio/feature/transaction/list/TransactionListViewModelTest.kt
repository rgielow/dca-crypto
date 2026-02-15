package com.cryptofolio.feature.transaction.list

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Exchange
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType
import com.cryptofolio.domain.repository.TransactionRepository
import com.cryptofolio.feature.transaction.usecase.DeleteTransactionUseCase
import com.cryptofolio.feature.transaction.usecase.GetTransactionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val now = Clock.System.now()

    private val testTransactions = listOf(
        Transaction(
            id = 1,
            coinId = "bitcoin",
            coinName = "Bitcoin",
            coinSymbol = "BTC",
            type = TransactionType.BUY,
            amount = 1.0,
            pricePerUnit = 50000.0,
            exchange = Exchange.BINANCE,
            currency = Currency.USD,
            date = now,
        ),
    )

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads transactions on init`() = runTest(testDispatcher) {
        val repository = object : TransactionRepository {
            override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(testTransactions)
            override fun getTransactionsByCoinId(coinId: String) = flowOf(testTransactions)
            override suspend fun getTransactionById(id: Long) = null
            override suspend fun insertTransaction(transaction: Transaction) {}
            override suspend fun updateTransaction(transaction: Transaction) {}
            override suspend fun deleteTransaction(id: Long) {}
            override suspend fun deleteAllTransactions() {}
        }

        val viewModel = TransactionListViewModel(
            getTransactions = GetTransactionsUseCase(repository),
            deleteTransaction = DeleteTransactionUseCase(repository),
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.transactions.size)
        assertEquals("bitcoin", state.transactions.first().coinId)
    }
}
