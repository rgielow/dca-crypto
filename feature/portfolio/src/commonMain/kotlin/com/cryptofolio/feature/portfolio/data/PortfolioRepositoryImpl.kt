package com.cryptofolio.feature.portfolio.data

import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Portfolio
import com.cryptofolio.domain.repository.AssetRepository
import com.cryptofolio.domain.repository.CoinPriceRepository
import com.cryptofolio.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class PortfolioRepositoryImpl(
    private val assetRepository: AssetRepository,
    private val coinPriceRepository: CoinPriceRepository,
) : PortfolioRepository {

    private val priceCache = MutableStateFlow<Map<String, Double>>(emptyMap())

    override fun getPortfolio(currency: Currency): Flow<Portfolio> =
        combine(assetRepository.getAllAssets(currency), priceCache) { assets, prices ->
            val updatedAssets = assets.map { asset ->
                val currentPrice = prices[asset.coinId] ?: asset.currentPrice
                asset.copy(
                    currentPrice = currentPrice,
                    currentValue = asset.totalAmount * currentPrice,
                    profitLoss = (asset.totalAmount * currentPrice) - asset.totalInvested,
                    profitLossPercentage = if (asset.totalInvested > 0) {
                        ((asset.totalAmount * currentPrice) - asset.totalInvested) / asset.totalInvested * 100
                    } else 0.0,
                )
            }
            Portfolio(assets = updatedAssets, currency = currency)
        }

    override suspend fun refreshPrices(currency: Currency): Result<Unit> = runCatching {
        val assets = mutableListOf<Asset>()
        assetRepository.getAllAssets(currency).collect { assets.addAll(it); return@collect }
        if (assets.isEmpty()) return@runCatching

        val coinIds = assets.map { it.coinId }
        val prices = coinPriceRepository.getCoinPrices(coinIds, currency).getOrThrow()
        priceCache.value = prices.associate { it.coinId to it.price }
    }
}
