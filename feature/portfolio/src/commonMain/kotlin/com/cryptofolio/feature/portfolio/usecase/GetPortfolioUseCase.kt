package com.cryptofolio.feature.portfolio.usecase

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow

class GetPortfolioUseCase(
    private val repository: PortfolioRepository,
) {
    operator fun invoke(currency: Currency = Currency.USD): Flow<Portfolio> =
        repository.getPortfolio(currency)
}
