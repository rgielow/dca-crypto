package com.cryptofolio.domain.model

import kotlinx.datetime.Clock
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
) {
    companion object {
        fun mock(
            id: Long = 1,
            coinId: String = "bitcoin",
            coinName: String = "Bitcoin",
            coinSymbol: String = "BTC",
            type: TransactionType = TransactionType.BUY,
            amount: Double = 1.0,
            pricePerUnit: Double = 50000.0,
            totalCost: Double = amount * pricePerUnit,
            fee: Double = 0.0,
            exchange: Exchange = Exchange.BINANCE,
            currency: Currency = Currency.USD,
            date: Instant = Clock.System.now(),
            notes: String = "",
        ) = Transaction(
            id = id,
            coinId = coinId,
            coinName = coinName,
            coinSymbol = coinSymbol,
            type = type,
            amount = amount,
            pricePerUnit = pricePerUnit,
            totalCost = totalCost,
            fee = fee,
            exchange = exchange,
            currency = currency,
            date = date,
            notes = notes,
        )
    }
}
