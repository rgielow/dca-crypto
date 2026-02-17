package com.cryptofolio.feature.portfolio.usecase

import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateWeightedAverageUseCaseTest {

    private val useCase = CalculateWeightedAverageUseCase()

    @Test
    fun `given empty transaction list - when invoke - then return zero`() {
        // Given
        val transactions = emptyList<Transaction>()

        // When
        val result = useCase(transactions)

        // Then
        assertEquals(0.0, result)
    }

    @Test
    fun `given single buy transaction - when invoke - then return that price`() {
        // Given
        val transactions = listOf(
            Transaction.mock(amount = 1.0, pricePerUnit = 50000.0),
        )

        // When
        val result = useCase(transactions)

        // Then
        assertEquals(50000.0, result)
    }

    @Test
    fun `given multiple buy transactions - when invoke - then return weighted average`() {
        // Given
        val transactions = listOf(
            Transaction.mock(id = 1, amount = 1.0, pricePerUnit = 40000.0, totalCost = 40000.0),
            Transaction.mock(id = 2, amount = 2.0, pricePerUnit = 50000.0, totalCost = 100000.0),
        )

        // When
        val result = useCase(transactions)

        // Then
        val expected = (40000.0 + 100000.0) / 3.0
        assertEquals(expected, result, 0.01)
    }

    @Test
    fun `given mix of buy and sell transactions - when invoke - then ignore sell transactions`() {
        // Given
        val transactions = listOf(
            Transaction.mock(id = 1, amount = 2.0, pricePerUnit = 40000.0, totalCost = 80000.0),
            Transaction.mock(id = 2, amount = 0.5, pricePerUnit = 60000.0, type = TransactionType.SELL),
        )

        // When
        val result = useCase(transactions)

        // Then
        assertEquals(40000.0, result)
    }

    @Test
    fun `given multiple buys at different prices - when invoke - then return correct weighted average`() {
        // Given
        val transactions = listOf(
            Transaction.mock(id = 1, amount = 0.5, pricePerUnit = 30000.0, totalCost = 15000.0),
            Transaction.mock(id = 2, amount = 0.3, pricePerUnit = 35000.0, totalCost = 10500.0),
            Transaction.mock(id = 3, amount = 0.2, pricePerUnit = 40000.0, totalCost = 8000.0),
        )

        // When
        val result = useCase(transactions)

        // Then
        val totalCost = 15000.0 + 10500.0 + 8000.0
        val totalAmount = 1.0
        assertEquals(totalCost / totalAmount, result, 0.01)
    }
}
