package com.cryptofolio.core.database.di

import com.cryptofolio.core.database.createDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDatabaseModule: Module = module {
    single { createDatabaseBuilder() }
}
