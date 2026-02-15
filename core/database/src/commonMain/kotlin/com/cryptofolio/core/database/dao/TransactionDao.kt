package com.cryptofolio.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cryptofolio.core.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE coinId = :coinId ORDER BY dateMillis DESC")
    fun getTransactionsByCoinId(coinId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    @Query("""
        SELECT coinId, coinName, coinSymbol, currency,
            SUM(CASE WHEN type = 'BUY' THEN amount ELSE -amount END) as totalAmount,
            SUM(CASE WHEN type = 'BUY' THEN totalCost ELSE 0 END) as totalInvested
        FROM transactions
        GROUP BY coinId
        HAVING totalAmount > 0
    """)
    fun getAggregatedAssets(): Flow<List<AggregatedAsset>>
}

data class AggregatedAsset(
    val coinId: String,
    val coinName: String,
    val coinSymbol: String,
    val currency: String,
    val totalAmount: Double,
    val totalInvested: Double,
)
