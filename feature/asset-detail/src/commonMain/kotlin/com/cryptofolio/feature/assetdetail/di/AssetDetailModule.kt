package com.cryptofolio.feature.assetdetail.di

import com.cryptofolio.feature.assetdetail.AssetDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val assetDetailModule = module {
    viewModelOf(::AssetDetailViewModel)
}
