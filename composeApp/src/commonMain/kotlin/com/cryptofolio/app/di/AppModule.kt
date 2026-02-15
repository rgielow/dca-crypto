package com.cryptofolio.app.di

import com.cryptofolio.core.database.di.databaseModule
import com.cryptofolio.core.network.di.networkModule
import com.cryptofolio.feature.assetdetail.di.assetDetailModule
import com.cryptofolio.feature.portfolio.di.portfolioModule
import com.cryptofolio.feature.settings.di.settingsModule
import com.cryptofolio.feature.transaction.di.transactionModule
import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        databaseModule,
        transactionModule,
        portfolioModule,
        assetDetailModule,
        settingsModule,
    )
}
