package com.cryptofolio

import android.app.Application
import com.cryptofolio.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CryptoFolioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CryptoFolioApplication)
            modules(appModule)
        }
    }
}
