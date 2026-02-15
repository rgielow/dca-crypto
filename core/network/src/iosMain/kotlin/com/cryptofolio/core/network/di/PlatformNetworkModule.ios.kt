package com.cryptofolio.core.network.di

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformNetworkModule = module {
    single<HttpClientEngineFactory<*>> { Darwin }
}
