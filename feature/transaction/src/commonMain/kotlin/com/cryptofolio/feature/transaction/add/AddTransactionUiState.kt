package com.cryptofolio.feature.transaction.add

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Exchange
import com.cryptofolio.domain.model.TransactionType

data class AddTransactionUiState(
    val coinId: String = "",
    val coinName: String = "",
    val coinSymbol: String = "",
    val type: TransactionType = TransactionType.BUY,
    val amount: String = "",
    val pricePerUnit: String = "",
    val fee: String = "",
    val exchange: Exchange = Exchange.BINANCE,
    val currency: Currency = Currency.USD,
    val notes: String = "",
    val isLoading: Boolean = false,
    val amountError: String? = null,
    val priceError: String? = null,
    val coinError: String? = null,
)
