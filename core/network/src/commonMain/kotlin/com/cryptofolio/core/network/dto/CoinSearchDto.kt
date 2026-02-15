package com.cryptofolio.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinSearchResponseDto(
    val coins: List<CoinSearchDto>,
)

@Serializable
data class CoinSearchDto(
    val id: String,
    val name: String,
    val symbol: String,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    val thumb: String? = null,
    val large: String? = null,
)
