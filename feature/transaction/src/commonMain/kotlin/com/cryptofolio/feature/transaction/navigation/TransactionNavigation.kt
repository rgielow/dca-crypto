package com.cryptofolio.feature.transaction.navigation

import kotlinx.serialization.Serializable

@Serializable
data object TransactionListRoute

@Serializable
data object AddTransactionRoute

@Serializable
data class TransactionDetailRoute(val transactionId: Long)
