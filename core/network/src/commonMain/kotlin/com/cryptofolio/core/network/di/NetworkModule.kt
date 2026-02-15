package com.cryptofolio.core.network.di

import com.cryptofolio.core.network.HttpClientFactory
import com.cryptofolio.core.network.api.CoinGeckoApi
import com.cryptofolio.core.network.api.CoinGeckoApiImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule = module {
    includes(platformNetworkModule)
    single { HttpClientFactory.create(get()) }
    single<CoinGeckoApi> { CoinGeckoApiImpl(get()) }
}

expect val platformNetworkModule: Module
