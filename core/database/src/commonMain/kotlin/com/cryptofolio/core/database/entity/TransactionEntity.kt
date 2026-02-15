package com.cryptofolio.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val coinId: String,
    val coinName: String,
    val coinSymbol: String,
    val type: String,
    val amount: Double,
    val pricePerUnit: Double,
    val totalCost: Double,
    val fee: Double,
    val exchange: String,
    val currency: String,
    val dateMillis: Long,
    val notes: String,
)
