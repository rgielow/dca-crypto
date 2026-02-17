package com.cryptofolio.feature.transaction.add

import app.cash.turbine.test
import com.cryptofolio.core.testing.runViewModelTest
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType
import com.cryptofolio.domain.repository.TransactionRepository
import com.cryptofolio.feature.transaction.usecase.AddTransactionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals

class AddTransactionViewModelTest {

    @Test
    fun `given initial state - when created - then state has default values`() =
        runViewModelTest {
            // Given / When
            val viewModel = createViewModel()

            // Then
            val expected = AddTransactionUiState()
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given UpdateCoinId action - when onAction - then coinId updated`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.onAction(AddTransactionAction.UpdateCoinId("bitcoin"))

            // Then
            assertEquals("bitcoin", viewModel.state.value.coinId)
        }

    @Test
    fun `given UpdateAmount action - when onAction - then amount updated and error cleared`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.onAction(AddTransactionAction.UpdateAmount("1.5"))

            // Then
            assertEquals("1.5", viewModel.state.value.amount)
            assertEquals(null, viewModel.state.value.amountError)
        }

    @Test
    fun `given UpdateType action - when onAction - then type updated`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()

            // When
            viewModel.onAction(AddTransactionAction.UpdateType(TransactionType.SELL))

            // Then
            assertEquals(TransactionType.SELL, viewModel.state.value.type)
        }

    @Test
    fun `given valid data - when Save action - then send TransactionSaved event`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()
            viewModel.onAction(AddTransactionAction.UpdateCoinId("bitcoin"))
            viewModel.onAction(AddTransactionAction.UpdateCoinName("Bitcoin"))
            viewModel.onAction(AddTransactionAction.UpdateCoinSymbol("BTC"))
            viewModel.onAction(AddTransactionAction.UpdateAmount("1.0"))
            viewModel.onAction(AddTransactionAction.UpdatePrice("50000"))

            // When / Then
            viewModel.events.test {
                viewModel.onAction(AddTransactionAction.Save)
                assertEquals(AddTransactionEvent.TransactionSaved, awaitItem())
            }
        }

    @Test
    fun `given empty coinId - when Save action - then state has coinError`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()
            viewModel.onAction(AddTransactionAction.UpdateAmount("1.0"))
            viewModel.onAction(AddTransactionAction.UpdatePrice("50000"))

            // When
            viewModel.onAction(AddTransactionAction.Save)

            // Then
            assertEquals("Coin is required", viewModel.state.value.coinError)
        }

    @Test
    fun `given empty amount - when Save action - then state has amountError`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()
            viewModel.onAction(AddTransactionAction.UpdateCoinId("bitcoin"))
            viewModel.onAction(AddTransactionAction.UpdatePrice("50000"))

            // When
            viewModel.onAction(AddTransactionAction.Save)

            // Then
            assertEquals("Valid amount required", viewModel.state.value.amountError)
        }

    @Test
    fun `given empty price - when Save action - then state has priceError`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()
            viewModel.onAction(AddTransactionAction.UpdateCoinId("bitcoin"))
            viewModel.onAction(AddTransactionAction.UpdateAmount("1.0"))

            // When
            viewModel.onAction(AddTransactionAction.Save)

            // Then
            assertEquals("Valid price required", viewModel.state.value.priceError)
        }

    private fun createViewModel(
        shouldFail: Boolean = false,
    ): AddTransactionViewModel {
        val repository = object : TransactionRepository {
            override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(emptyList())
            override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> =
                flowOf(emptyList())
            override suspend fun getTransactionById(id: Long): Transaction? = null
            override suspend fun insertTransaction(transaction: Transaction) {
                if (shouldFail) throw Exception("Insert failed")
            }
            override suspend fun updateTransaction(transaction: Transaction) {}
            override suspend fun deleteTransaction(id: Long) {}
            override suspend fun deleteAllTransactions() {}
        }
        return AddTransactionViewModel(AddTransactionUseCase(repository))
    }
}
