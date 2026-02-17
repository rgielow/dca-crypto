package com.cryptofolio.feature.transaction.list

import app.cash.turbine.test
import com.cryptofolio.core.testing.runViewModelTest
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import com.cryptofolio.feature.transaction.usecase.DeleteTransactionUseCase
import com.cryptofolio.feature.transaction.usecase.GetTransactionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionListViewModelTest {

    @Test
    fun `given repository has transactions - when initialized - then state updated with transactions`() =
        runViewModelTest {
            // Given
            val testTransactions = listOf(Transaction.mock())
            val repository = FakeTransactionRepository(transactions = testTransactions)

            // When
            val viewModel = createViewModel(repository)

            // Then
            val expected = TransactionListUiState(
                transactions = testTransactions,
                isLoading = false,
                error = null,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given empty repository - when initialized - then state has empty list`() =
        runViewModelTest {
            // Given
            val repository = FakeTransactionRepository(transactions = emptyList())

            // When
            val viewModel = createViewModel(repository)

            // Then
            val expected = TransactionListUiState(
                transactions = emptyList(),
                isLoading = false,
                error = null,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given AddTransaction action - when onAction - then send NavigateToAddTransaction event`() =
        runViewModelTest {
            // Given
            val repository = FakeTransactionRepository()
            val viewModel = createViewModel(repository)

            // When / Then
            viewModel.events.test {
                viewModel.onAction(TransactionListAction.AddTransaction)
                assertEquals(TransactionListEvent.NavigateToAddTransaction, awaitItem())
            }
        }

    @Test
    fun `given SelectTransaction action - when onAction - then send NavigateToTransactionDetail event`() =
        runViewModelTest {
            // Given
            val repository = FakeTransactionRepository()
            val viewModel = createViewModel(repository)

            // When / Then
            viewModel.events.test {
                viewModel.onAction(TransactionListAction.SelectTransaction(1))
                assertEquals(TransactionListEvent.NavigateToTransactionDetail(1), awaitItem())
            }
        }

    @Test
    fun `given successful delete - when DeleteTransaction action - then send TransactionDeleted event`() =
        runViewModelTest {
            // Given
            val repository = FakeTransactionRepository()
            val viewModel = createViewModel(repository)

            // When / Then
            viewModel.events.test {
                viewModel.onAction(TransactionListAction.DeleteTransaction(1))
                assertEquals(TransactionListEvent.TransactionDeleted, awaitItem())
            }
        }

    private fun createViewModel(repository: TransactionRepository) = TransactionListViewModel(
        getTransactions = GetTransactionsUseCase(repository),
        deleteTransaction = DeleteTransactionUseCase(repository),
    )
}

private class FakeTransactionRepository(
    private val transactions: List<Transaction> = listOf(Transaction.mock()),
    private val deleteResult: Result<Unit> = Result.success(Unit),
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(transactions)
    override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> =
        flowOf(transactions.filter { it.coinId == coinId })
    override suspend fun getTransactionById(id: Long): Transaction? =
        transactions.find { it.id == id }
    override suspend fun insertTransaction(transaction: Transaction) {}
    override suspend fun updateTransaction(transaction: Transaction) {}
    override suspend fun deleteTransaction(id: Long) { deleteResult.getOrThrow() }
    override suspend fun deleteAllTransactions() {}
}
