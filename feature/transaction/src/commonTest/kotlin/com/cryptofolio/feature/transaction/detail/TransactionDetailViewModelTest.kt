package com.cryptofolio.feature.transaction.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.TransactionRepository
import com.cryptofolio.feature.transaction.usecase.DeleteTransactionUseCase
import com.cryptofolio.feature.transaction.usecase.GetTransactionByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given transaction exists - when initialized - then state updated with transaction`() =
        runTest(testDispatcher) {
            // Given
            val transaction = Transaction.mock(id = 1)
            val viewModel = createViewModel(transaction = transaction)

            // When
            advanceUntilIdle()

            // Then
            val expected = TransactionDetailUiState(
                transaction = transaction,
                isLoading = false,
                error = null,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given transaction not found - when initialized - then state has error`() =
        runTest(testDispatcher) {
            // Given
            val viewModel = createViewModel(transaction = null)

            // When
            advanceUntilIdle()

            // Then
            assertEquals(false, viewModel.state.value.isLoading)
            assertEquals("Transaction not found", viewModel.state.value.error)
        }

    @Test
    fun `given NavigateBack action - when onAction - then send NavigateBack event`() =
        runTest(testDispatcher) {
            // Given
            val viewModel = createViewModel(transaction = Transaction.mock(id = 1))
            advanceUntilIdle()

            // When / Then
            viewModel.events.test {
                viewModel.onAction(TransactionDetailAction.NavigateBack)
                assertEquals(TransactionDetailEvent.NavigateBack, awaitItem())
            }
        }

    @Test
    fun `given successful delete - when Delete action - then send NavigateBack event`() =
        runTest(testDispatcher) {
            // Given
            val viewModel = createViewModel(transaction = Transaction.mock(id = 1))
            advanceUntilIdle()

            // When / Then
            viewModel.events.test {
                viewModel.onAction(TransactionDetailAction.Delete)
                advanceUntilIdle()
                assertEquals(TransactionDetailEvent.NavigateBack, awaitItem())
            }
        }

    @Test
    fun `given failed delete - when Delete action - then send ShowError event`() =
        runTest(testDispatcher) {
            // Given
            val viewModel = createViewModel(
                transaction = Transaction.mock(id = 1),
                shouldFailDelete = true,
            )
            advanceUntilIdle()

            // When / Then
            viewModel.events.test {
                viewModel.onAction(TransactionDetailAction.Delete)
                advanceUntilIdle()
                assertEquals(TransactionDetailEvent.ShowError("Delete failed"), awaitItem())
            }
        }

    private fun createViewModel(
        transactionId: Long = 1L,
        transaction: Transaction? = Transaction.mock(id = 1),
        shouldFailDelete: Boolean = false,
    ): TransactionDetailViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("transactionId" to transactionId))
        val repository = object : TransactionRepository {
            override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(emptyList())
            override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> =
                flowOf(emptyList())
            override suspend fun getTransactionById(id: Long): Transaction? = transaction
            override suspend fun insertTransaction(transaction: Transaction) {}
            override suspend fun updateTransaction(transaction: Transaction) {}
            override suspend fun deleteTransaction(id: Long) {
                if (shouldFailDelete) throw Exception("Delete failed")
            }
            override suspend fun deleteAllTransactions() {}
        }
        return TransactionDetailViewModel(
            savedStateHandle = savedStateHandle,
            getTransactionById = GetTransactionByIdUseCase(repository),
            deleteTransaction = DeleteTransactionUseCase(repository),
        )
    }
}
