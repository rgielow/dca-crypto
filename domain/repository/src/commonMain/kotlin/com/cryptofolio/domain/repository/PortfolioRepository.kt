package com.cryptofolio.domain.repository

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    fun getPortfolio(currency: Currency): Flow<Portfolio>
    suspend fun refreshPrices(currency: Currency): Result<Unit>
}
