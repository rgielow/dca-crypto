package com.cryptofolio.feature.transaction.data.mapper

import com.cryptofolio.core.database.entity.TransactionEntity
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Exchange
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType
import kotlinx.datetime.Instant

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    coinId = coinId,
    coinName = coinName,
    coinSymbol = coinSymbol,
    type = TransactionType.valueOf(type),
    amount = amount,
    pricePerUnit = pricePerUnit,
    totalCost = totalCost,
    fee = fee,
    exchange = try { Exchange.valueOf(exchange) } catch (_: Exception) { Exchange.OTHER },
    currency = try { Currency.valueOf(currency) } catch (_: Exception) { Currency.USD },
    date = Instant.fromEpochMilliseconds(dateMillis),
    notes = notes,
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    coinId = coinId,
    coinName = coinName,
    coinSymbol = coinSymbol,
    type = type.name,
    amount = amount,
    pricePerUnit = pricePerUnit,
    totalCost = totalCost,
    fee = fee,
    exchange = exchange.name,
    currency = currency.name,
    dateMillis = date.toEpochMilliseconds(),
    notes = notes,
)
