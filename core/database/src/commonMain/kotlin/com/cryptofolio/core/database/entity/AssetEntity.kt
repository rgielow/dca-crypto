package com.cryptofolio.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey val coinId: String,
    val coinName: String,
    val coinSymbol: String,
    val totalAmount: Double,
    val averageBuyPrice: Double,
    val totalInvested: Double,
    val currency: String,
    val lastUpdated: Long,
)
