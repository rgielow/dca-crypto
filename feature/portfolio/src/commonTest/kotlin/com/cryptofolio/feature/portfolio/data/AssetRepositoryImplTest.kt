package com.cryptofolio.feature.portfolio.data

import app.cash.turbine.test
import com.cryptofolio.core.database.dao.AggregatedAsset
import com.cryptofolio.core.database.dao.TransactionDao
import com.cryptofolio.core.database.entity.TransactionEntity
import com.cryptofolio.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AssetRepositoryImplTest {

    @Test
    fun `given aggregated assets exist - when getAllAssets - then return mapped assets`() = runTest {
        // Given
        val aggregatedAssets = listOf(
            AggregatedAsset(
                coinId = "bitcoin",
                coinName = "Bitcoin",
                coinSymbol = "BTC",
                currency = "USD",
                totalAmount = 1.5,
                totalInvested = 75000.0,
            ),
        )
        val repository = AssetRepositoryImpl(FakeTransactionDao(aggregatedAssets = aggregatedAssets))

        // When / Then
        repository.getAllAssets(Currency.USD).test {
            val assets = awaitItem()
            assertEquals(1, assets.size)
            assertEquals("bitcoin", assets.first().coinId)
            assertEquals(1.5, assets.first().totalAmount)
            assertEquals(75000.0, assets.first().totalInvested)
            awaitComplete()
        }
    }

    @Test
    fun `given asset exists - when getAssetByCoinId - then return matching asset`() = runTest {
        // Given
        val aggregatedAssets = listOf(
            AggregatedAsset("bitcoin", "Bitcoin", "BTC", "USD", 1.0, 50000.0),
            AggregatedAsset("ethereum", "Ethereum", "ETH", "USD", 10.0, 30000.0),
        )
        val repository = AssetRepositoryImpl(FakeTransactionDao(aggregatedAssets = aggregatedAssets))

        // When / Then
        repository.getAssetByCoinId("ethereum", Currency.USD).test {
            val asset = awaitItem()
            assertEquals("ethereum", asset?.coinId)
            assertEquals(10.0, asset?.totalAmount)
            awaitComplete()
        }
    }

    @Test
    fun `given asset not found - when getAssetByCoinId - then return null`() = runTest {
        // Given
        val repository = AssetRepositoryImpl(FakeTransactionDao(aggregatedAssets = emptyList()))

        // When / Then
        repository.getAssetByCoinId("unknown", Currency.USD).test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }
}

private class FakeTransactionDao(
    private val aggregatedAssets: List<AggregatedAsset> = emptyList(),
) : TransactionDao {
    override fun getAllTransactions(): Flow<List<TransactionEntity>> = flowOf(emptyList())
    override fun getTransactionsByCoinId(coinId: String): Flow<List<TransactionEntity>> = flowOf(emptyList())
    override suspend fun getTransactionById(id: Long): TransactionEntity? = null
    override suspend fun insertTransaction(transaction: TransactionEntity) {}
    override suspend fun updateTransaction(transaction: TransactionEntity) {}
    override suspend fun deleteTransaction(id: Long) {}
    override suspend fun deleteAllTransactions() {}
    override fun getAggregatedAssets(): Flow<List<AggregatedAsset>> = flowOf(aggregatedAssets)
}
