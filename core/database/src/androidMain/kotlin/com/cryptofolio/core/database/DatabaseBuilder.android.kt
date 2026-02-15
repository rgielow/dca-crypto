package com.cryptofolio.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun createDatabaseBuilder(context: Context): RoomDatabase.Builder<CryptoFolioDatabase> {
    val dbFile = context.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<CryptoFolioDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
}
