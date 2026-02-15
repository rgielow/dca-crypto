package com.cryptofolio.feature.portfolio.di

import com.cryptofolio.domain.repository.AssetRepository
import com.cryptofolio.domain.repository.CoinPriceRepository
import com.cryptofolio.domain.repository.PortfolioRepository
import com.cryptofolio.feature.portfolio.data.AssetRepositoryImpl
import com.cryptofolio.feature.portfolio.data.CoinPriceRepositoryImpl
import com.cryptofolio.feature.portfolio.data.PortfolioRepositoryImpl
import com.cryptofolio.feature.portfolio.overview.PortfolioViewModel
import com.cryptofolio.feature.portfolio.usecase.CalculateWeightedAverageUseCase
import com.cryptofolio.feature.portfolio.usecase.GetPortfolioUseCase
import com.cryptofolio.feature.portfolio.usecase.RefreshPricesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val portfolioModule = module {
    singleOf(::AssetRepositoryImpl) bind AssetRepository::class
    singleOf(::CoinPriceRepositoryImpl) bind CoinPriceRepository::class
    singleOf(::PortfolioRepositoryImpl) bind PortfolioRepository::class
    factoryOf(::GetPortfolioUseCase)
    factoryOf(::CalculateWeightedAverageUseCase)
    factoryOf(::RefreshPricesUseCase)
    viewModelOf(::PortfolioViewModel)
}
