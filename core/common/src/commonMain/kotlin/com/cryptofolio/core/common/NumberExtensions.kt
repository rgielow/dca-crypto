package com.cryptofolio.core.common

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return (this * factor).roundToInt() / factor
}

fun Double.toPercentageString(): String {
    val rounded = this.roundTo(2)
    val sign = if (rounded >= 0) "+" else ""
    return "$sign${rounded}%"
}

fun Double.toCurrencyString(symbol: String = "$", decimals: Int = 2): String {
    val rounded = this.absoluteValue.roundTo(decimals)
    val sign = if (this < 0) "-" else ""
    return "$sign$symbol$rounded"
}
