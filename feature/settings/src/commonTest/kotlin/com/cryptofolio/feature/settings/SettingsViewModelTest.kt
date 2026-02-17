package com.cryptofolio.feature.settings

import app.cash.turbine.test
import com.cryptofolio.domain.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given initial state - when created - then state has default values`() =
        runTest(testDispatcher) {
            // Given / When
            val viewModel = SettingsViewModel()
            advanceUntilIdle()

            // Then
            val expected = SettingsUiState(
                selectedCurrency = Currency.USD,
                isDarkTheme = false,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given UpdateCurrency action - when onAction - then state updated and event sent`() =
        runTest(testDispatcher) {
            // Given
            val viewModel = SettingsViewModel()
            advanceUntilIdle()

            // When
            viewModel.events.test {
                viewModel.onAction(SettingsAction.UpdateCurrency(Currency.BRL))
                advanceUntilIdle()

                // Then
                assertEquals(Currency.BRL, viewModel.state.value.selectedCurrency)
                assertEquals(SettingsEvent.ShowMessage("Currency updated"), awaitItem())
            }
        }

    @Test
    fun `given UpdateTheme action with true - when onAction - then isDarkTheme is true`() =
        runTest(testDispatcher) {
            // Given
            val viewModel = SettingsViewModel()
            advanceUntilIdle()

            // When
            viewModel.onAction(SettingsAction.UpdateTheme(true))
            advanceUntilIdle()

            // Then
            val expected = SettingsUiState(
                selectedCurrency = Currency.USD,
                isDarkTheme = true,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given UpdateTheme action with false - when onAction - then isDarkTheme is false`() =
        runTest(testDispatcher) {
            // Given
            val viewModel = SettingsViewModel()
            viewModel.onAction(SettingsAction.UpdateTheme(true))
            advanceUntilIdle()

            // When
            viewModel.onAction(SettingsAction.UpdateTheme(false))
            advanceUntilIdle()

            // Then
            val expected = SettingsUiState(
                selectedCurrency = Currency.USD,
                isDarkTheme = false,
            )
            assertEquals(expected, viewModel.state.value)
        }
}
