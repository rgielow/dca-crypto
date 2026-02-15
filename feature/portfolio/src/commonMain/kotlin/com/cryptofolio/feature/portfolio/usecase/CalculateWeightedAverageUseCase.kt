package com.cryptofolio.feature.portfolio.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType

class CalculateWeightedAverageUseCase {
    operator fun invoke(transactions: List<Transaction>): Double {
        val buys = transactions.filter { it.type == TransactionType.BUY }
        if (buys.isEmpty()) return 0.0

        val totalCost = buys.sumOf { it.totalCost }
        val totalAmount = buys.sumOf { it.amount }

        return if (totalAmount > 0) totalCost / totalAmount else 0.0
    }
}
