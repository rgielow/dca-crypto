package com.cryptofolio.domain.model

enum class Currency(val symbol: String, val code: String) {
    USD("$", "usd"),
    BRL("R$", "brl"),
    EUR("€", "eur");
}
