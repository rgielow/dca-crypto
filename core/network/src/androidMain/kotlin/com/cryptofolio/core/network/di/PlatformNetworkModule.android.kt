package com.cryptofolio.core.network.di

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformNetworkModule = module {
    single<HttpClientEngineFactory<*>> { OkHttp }
}
