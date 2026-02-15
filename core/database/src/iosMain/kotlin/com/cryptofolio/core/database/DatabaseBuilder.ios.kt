package com.cryptofolio.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun createDatabaseBuilder(): RoomDatabase.Builder<CryptoFolioDatabase> {
    val dbFilePath = NSHomeDirectory() + "/$DATABASE_NAME"
    return Room.databaseBuilder<CryptoFolioDatabase>(
        name = dbFilePath,
    )
}
