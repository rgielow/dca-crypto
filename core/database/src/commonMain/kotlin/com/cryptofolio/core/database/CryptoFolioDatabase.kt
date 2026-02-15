package com.cryptofolio.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.cryptofolio.core.database.dao.AssetDao
import com.cryptofolio.core.database.dao.TransactionDao
import com.cryptofolio.core.database.entity.AssetEntity
import com.cryptofolio.core.database.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, AssetEntity::class],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(CryptoFolioDatabaseConstructor::class)
abstract class CryptoFolioDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun assetDao(): AssetDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CryptoFolioDatabaseConstructor : RoomDatabaseConstructor<CryptoFolioDatabase> {
    override fun initialize(): CryptoFolioDatabase
}
