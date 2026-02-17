package com.cryptofolio.feature.assetdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.cryptofolio.core.testing.runViewModelTest
import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.repository.AssetRepository
import com.cryptofolio.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals

class AssetDetailViewModelTest {

    @Test
    fun `given asset exists - when initialized - then state updated with asset and transactions`() =
        runViewModelTest {
            // Given
            val asset = Asset.mock(coinId = "bitcoin")
            val transactions = listOf(Transaction.mock(coinId = "bitcoin"))
            val viewModel = createViewModel(
                asset = asset,
                transactions = transactions,
            )

            // Then
            val expected = AssetDetailUiState(
                asset = asset,
                transactions = transactions,
                isLoading = false,
                error = null,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given asset not found - when initialized - then state has null asset`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel(asset = null, transactions = emptyList())

            // Then
            val expected = AssetDetailUiState(
                asset = null,
                transactions = emptyList(),
                isLoading = false,
                error = null,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given NavigateBack action - when onAction - then send NavigateBack event`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()

            // When / Then
            viewModel.events.test {
                viewModel.onAction(AssetDetailAction.NavigateBack)
                assertEquals(AssetDetailEvent.NavigateBack, awaitItem())
            }
        }

    @Test
    fun `given SelectTransaction action - when onAction - then send NavigateToTransaction event`() =
        runViewModelTest {
            // Given
            val viewModel = createViewModel()

            // When / Then
            viewModel.events.test {
                viewModel.onAction(AssetDetailAction.SelectTransaction(42))
                assertEquals(AssetDetailEvent.NavigateToTransaction(42), awaitItem())
            }
        }

    private fun createViewModel(
        coinId: String = "bitcoin",
        asset: Asset? = Asset.mock(),
        transactions: List<Transaction> = listOf(Transaction.mock()),
    ): AssetDetailViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("coinId" to coinId))
        val assetRepository = object : AssetRepository {
            override fun getAllAssets(currency: Currency): Flow<List<Asset>> =
                flowOf(listOfNotNull(asset))
            override fun getAssetByCoinId(coinId: String, currency: Currency): Flow<Asset?> =
                flowOf(asset)
        }
        val transactionRepository = object : TransactionRepository {
            override fun getAllTransactions(): Flow<List<Transaction>> = flowOf(transactions)
            override fun getTransactionsByCoinId(coinId: String): Flow<List<Transaction>> =
                flowOf(transactions)
            override suspend fun getTransactionById(id: Long): Transaction? = null
            override suspend fun insertTransaction(transaction: Transaction) {}
            override suspend fun updateTransaction(transaction: Transaction) {}
            override suspend fun deleteTransaction(id: Long) {}
            override suspend fun deleteAllTransactions() {}
        }
        return AssetDetailViewModel(savedStateHandle, assetRepository, transactionRepository)
    }
}
