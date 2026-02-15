package com.cryptofolio.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getDatabaseBuilder(builder: RoomDatabase.Builder<CryptoFolioDatabase>): CryptoFolioDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

internal const val DATABASE_NAME = "cryptofolio.db"
