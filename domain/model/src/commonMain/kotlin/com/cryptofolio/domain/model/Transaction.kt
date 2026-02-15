package com.cryptofolio.domain.model

import kotlinx.datetime.Instant

data class Transaction(
    val id: Long = 0,
    val coinId: String,
    val coinName: String,
    val coinSymbol: String,
    val type: TransactionType,
    val amount: Double,
    val pricePerUnit: Double,
    val totalCost: Double = amount * pricePerUnit,
    val fee: Double = 0.0,
    val exchange: Exchange,
    val currency: Currency,
    val date: Instant,
    val notes: String = "",
)
