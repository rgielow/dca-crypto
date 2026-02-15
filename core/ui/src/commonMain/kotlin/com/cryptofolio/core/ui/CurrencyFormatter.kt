package com.cryptofolio.core.ui

import com.cryptofolio.domain.model.Currency
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToLong

object CurrencyFormatter {
    fun format(amount: Double, currency: Currency, showSign: Boolean = false): String {
        val rounded = roundToDecimals(amount.absoluteValue, 2)
        val sign = when {
            !showSign -> if (amount < 0) "-" else ""
            amount >= 0 -> "+"
            else -> "-"
        }
        return "$sign${currency.symbol}${formatNumber(rounded)}"
    }

    fun formatCrypto(amount: Double, symbol: String, decimals: Int = 8): String {
        val rounded = roundToDecimals(amount, decimals)
        return "$rounded $symbol"
    }

    private fun roundToDecimals(value: Double, decimals: Int): String {
        val factor = 10.0.pow(decimals).toLong()
        val rounded = (value * factor).roundToLong().toDouble() / factor
        return buildString {
            val parts = rounded.toString().split(".")
            append(parts[0])
            append(".")
            val decimal = if (parts.size > 1) parts[1] else ""
            append(decimal.padEnd(decimals, '0').take(decimals))
        }
    }

    private fun formatNumber(value: String): String {
        val parts = value.split(".")
        val intPart = parts[0].reversed().chunked(3).joinToString(",").reversed()
        return if (parts.size > 1) "$intPart.${parts[1]}" else intPart
    }
}
