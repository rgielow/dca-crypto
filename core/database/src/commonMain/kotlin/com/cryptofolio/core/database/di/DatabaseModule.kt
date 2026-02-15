package com.cryptofolio.core.database.di

import com.cryptofolio.core.database.CryptoFolioDatabase
import com.cryptofolio.core.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule = module {
    includes(platformDatabaseModule)
    single { getDatabaseBuilder(get()) }
    single { get<CryptoFolioDatabase>().transactionDao() }
    single { get<CryptoFolioDatabase>().assetDao() }
}

expect val platformDatabaseModule: Module
