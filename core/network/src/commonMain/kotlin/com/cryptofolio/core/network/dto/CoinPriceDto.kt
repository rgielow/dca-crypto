package com.cryptofolio.core.network.dto

import kotlinx.serialization.Serializable

typealias CoinPriceResponse = Map<String, CoinPriceDetailDto>

@Serializable
data class CoinPriceDetailDto(
    val usd: Double? = null,
    val brl: Double? = null,
    val eur: Double? = null,
    val usd_24h_change: Double? = null,
    val brl_24h_change: Double? = null,
    val eur_24h_change: Double? = null,
)
