package com.cryptofolio.feature.portfolio.data.mapper

import com.cryptofolio.core.database.dao.AggregatedAsset
import com.cryptofolio.domain.model.Asset
import com.cryptofolio.domain.model.Currency

fun AggregatedAsset.toDomain(): Asset = Asset(
    coinId = coinId,
    coinName = coinName,
    coinSymbol = coinSymbol,
    totalAmount = totalAmount,
    averageBuyPrice = if (totalAmount > 0) totalInvested / totalAmount else 0.0,
    totalInvested = totalInvested,
    currency = try { Currency.valueOf(currency) } catch (_: Exception) { Currency.USD },
)
