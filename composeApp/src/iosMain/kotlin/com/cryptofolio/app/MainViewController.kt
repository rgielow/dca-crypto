package com.cryptofolio.app

import androidx.compose.ui.window.ComposeUIViewController
import com.cryptofolio.app.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
