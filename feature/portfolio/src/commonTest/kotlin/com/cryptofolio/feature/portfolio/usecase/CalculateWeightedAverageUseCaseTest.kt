package com.cryptofolio.feature.portfolio.usecase

import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.Exchange
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateWeightedAverageUseCaseTest {

    private val useCase = CalculateWeightedAverageUseCase()
    private val now = Clock.System.now()

    @Test
    fun `returns zero for empty list`() {
        val result = useCase(emptyList())
        assertEquals(0.0, result)
    }

    @Test
    fun `calculates average for single buy`() {
        val transactions = listOf(
            createTransaction(amount = 1.0, pricePerUnit = 50000.0),
        )
        val result = useCase(transactions)
        assertEquals(50000.0, result)
    }

    @Test
    fun `calculates weighted average for multiple buys`() {
        val transactions = listOf(
            createTransaction(amount = 1.0, pricePerUnit = 40000.0),
            createTransaction(amount = 2.0, pricePerUnit = 50000.0),
        )
        val result = useCase(transactions)
        val expected = (40000.0 + 100000.0) / 3.0
        assertEquals(expected, result, 0.01)
    }

    @Test
    fun `ignores sell transactions`() {
        val transactions = listOf(
            createTransaction(amount = 2.0, pricePerUnit = 40000.0),
            createTransaction(amount = 0.5, pricePerUnit = 60000.0, type = TransactionType.SELL),
        )
        val result = useCase(transactions)
        assertEquals(40000.0, result)
    }

    @Test
    fun `handles multiple buys at different prices`() {
        val transactions = listOf(
            createTransaction(amount = 0.5, pricePerUnit = 30000.0),
            createTransaction(amount = 0.3, pricePerUnit = 35000.0),
            createTransaction(amount = 0.2, pricePerUnit = 40000.0),
        )
        val result = useCase(transactions)
        val totalCost = (0.5 * 30000.0) + (0.3 * 35000.0) + (0.2 * 40000.0)
        val totalAmount = 1.0
        assertEquals(totalCost / totalAmount, result, 0.01)
    }

    private fun createTransaction(
        amount: Double,
        pricePerUnit: Double,
        type: TransactionType = TransactionType.BUY,
    ) = Transaction(
        coinId = "bitcoin",
        coinName = "Bitcoin",
        coinSymbol = "BTC",
        type = type,
        amount = amount,
        pricePerUnit = pricePerUnit,
        totalCost = amount * pricePerUnit,
        exchange = Exchange.BINANCE,
        currency = Currency.USD,
        date = now,
    )
}
