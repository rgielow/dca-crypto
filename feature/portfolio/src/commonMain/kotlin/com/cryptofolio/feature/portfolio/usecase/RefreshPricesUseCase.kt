package com.cryptofolio.feature.portfolio.usecase

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.repository.PortfolioRepository

class RefreshPricesUseCase(
    private val repository: PortfolioRepository,
) {
    suspend operator fun invoke(currency: Currency = Currency.USD): Result<Unit> =
        repository.refreshPrices(currency)
}
