package com.cryptofolio.feature.transaction.data

import app.cash.turbine.test
import com.cryptofolio.core.database.dao.AggregatedAsset
import com.cryptofolio.core.database.dao.TransactionDao
import com.cryptofolio.core.database.entity.TransactionEntity
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TransactionRepositoryImplTest {

    @Test
    fun `given entities exist - when getAllTransactions - then return mapped transactions`() =
        runTest {
            // Given
            val entities = listOf(createEntity(id = 1))
            val repository = TransactionRepositoryImpl(FakeTransactionDao(entities = entities))

            // When / Then
            repository.getAllTransactions().test {
                val transactions = awaitItem()
                assertEquals(1, transactions.size)
                assertEquals("bitcoin", transactions.first().coinId)
                assertEquals(TransactionType.BUY, transactions.first().type)
                awaitComplete()
            }
        }

    @Test
    fun `given entities exist - when getTransactionsByCoinId - then return filtered transactions`() =
        runTest {
            // Given
            val entities = listOf(createEntity(id = 1, coinId = "bitcoin"))
            val dao = FakeTransactionDao(coinEntities = entities)
            val repository = TransactionRepositoryImpl(dao)

            // When / Then
            repository.getTransactionsByCoinId("bitcoin").test {
                val transactions = awaitItem()
                assertEquals(1, transactions.size)
                assertEquals("bitcoin", transactions.first().coinId)
                awaitComplete()
            }
        }

    @Test
    fun `given entity exists - when getTransactionById - then return mapped transaction`() =
        runTest {
            // Given
            val entity = createEntity(id = 1)
            val repository = TransactionRepositoryImpl(FakeTransactionDao(singleEntity = entity))

            // When
            val transaction = repository.getTransactionById(1)

            // Then
            assertNotNull(transaction)
            assertEquals("bitcoin", transaction.coinId)
            assertEquals(1L, transaction.id)
        }

    @Test
    fun `given entity not found - when getTransactionById - then return null`() = runTest {
        // Given
        val repository = TransactionRepositoryImpl(FakeTransactionDao())

        // When
        val transaction = repository.getTransactionById(999)

        // Then
        assertNull(transaction)
    }

    @Test
    fun `given transaction - when insertTransaction - then dao receives entity`() = runTest {
        // Given
        val dao = FakeTransactionDao()
        val repository = TransactionRepositoryImpl(dao)
        val transaction = Transaction.mock()

        // When
        repository.insertTransaction(transaction)

        // Then
        assertTrue(dao.insertedEntities.isNotEmpty())
        assertEquals("bitcoin", dao.insertedEntities.first().coinId)
    }

    @Test
    fun `given id - when deleteTransaction - then dao receives id`() = runTest {
        // Given
        val dao = FakeTransactionDao()
        val repository = TransactionRepositoryImpl(dao)

        // When
        repository.deleteTransaction(5L)

        // Then
        assertEquals(5L, dao.deletedId)
    }

    private fun createEntity(
        id: Long = 1,
        coinId: String = "bitcoin",
    ) = TransactionEntity(
        id = id,
        coinId = coinId,
        coinName = "Bitcoin",
        coinSymbol = "BTC",
        type = "BUY",
        amount = 1.0,
        pricePerUnit = 50000.0,
        totalCost = 50000.0,
        fee = 0.0,
        exchange = "BINANCE",
        currency = "USD",
        dateMillis = 1700000000000L,
        notes = "",
    )
}

private class FakeTransactionDao(
    private val entities: List<TransactionEntity> = emptyList(),
    private val coinEntities: List<TransactionEntity> = emptyList(),
    private val singleEntity: TransactionEntity? = null,
) : TransactionDao {
    val insertedEntities = mutableListOf<TransactionEntity>()
    var deletedId: Long? = null

    override fun getAllTransactions(): Flow<List<TransactionEntity>> = flowOf(entities)
    override fun getTransactionsByCoinId(coinId: String): Flow<List<TransactionEntity>> = flowOf(coinEntities)
    override suspend fun getTransactionById(id: Long): TransactionEntity? = singleEntity
    override suspend fun insertTransaction(transaction: TransactionEntity) {
        insertedEntities.add(transaction)
    }
    override suspend fun updateTransaction(transaction: TransactionEntity) {}
    override suspend fun deleteTransaction(id: Long) { deletedId = id }
    override suspend fun deleteAllTransactions() {}
    override fun getAggregatedAssets(): Flow<List<AggregatedAsset>> = flowOf(emptyList())
}
