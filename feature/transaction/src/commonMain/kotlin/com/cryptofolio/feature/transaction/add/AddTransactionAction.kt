package com.cryptofolio.feature.transaction.add

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Exchange
import com.cryptofolio.domain.model.TransactionType

sealed interface AddTransactionAction {
    data class UpdateCoinId(val coinId: String) : AddTransactionAction
    data class UpdateCoinName(val name: String) : AddTransactionAction
    data class UpdateCoinSymbol(val symbol: String) : AddTransactionAction
    data class UpdateType(val type: TransactionType) : AddTransactionAction
    data class UpdateAmount(val amount: String) : AddTransactionAction
    data class UpdatePrice(val price: String) : AddTransactionAction
    data class UpdateFee(val fee: String) : AddTransactionAction
    data class UpdateExchange(val exchange: Exchange) : AddTransactionAction
    data class UpdateCurrency(val currency: Currency) : AddTransactionAction
    data class UpdateNotes(val notes: String) : AddTransactionAction
    data object Save : AddTransactionAction
}
