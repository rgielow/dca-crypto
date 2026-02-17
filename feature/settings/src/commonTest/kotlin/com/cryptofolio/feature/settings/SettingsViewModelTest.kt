package com.cryptofolio.feature.settings

import app.cash.turbine.test
import com.cryptofolio.core.testing.runViewModelTest
import com.cryptofolio.domain.model.Currency
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsViewModelTest {

    @Test
    fun `given initial state - when created - then state has default values`() =
        runViewModelTest {
            // Given / When
            val viewModel = SettingsViewModel()

            // Then
            val expected = SettingsUiState(
                selectedCurrency = Currency.USD,
                isDarkTheme = false,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given UpdateCurrency action - when onAction - then state updated and event sent`() =
        runViewModelTest {
            // Given
            val viewModel = SettingsViewModel()

            // When
            viewModel.events.test {
                viewModel.onAction(SettingsAction.UpdateCurrency(Currency.BRL))

                // Then
                assertEquals(Currency.BRL, viewModel.state.value.selectedCurrency)
                assertEquals(SettingsEvent.ShowMessage("Currency updated"), awaitItem())
            }
        }

    @Test
    fun `given UpdateTheme action with true - when onAction - then isDarkTheme is true`() =
        runViewModelTest {
            // Given
            val viewModel = SettingsViewModel()

            // When
            viewModel.onAction(SettingsAction.UpdateTheme(true))

            // Then
            val expected = SettingsUiState(
                selectedCurrency = Currency.USD,
                isDarkTheme = true,
            )
            assertEquals(expected, viewModel.state.value)
        }

    @Test
    fun `given UpdateTheme action with false - when onAction - then isDarkTheme is false`() =
        runViewModelTest {
            // Given
            val viewModel = SettingsViewModel()
            viewModel.onAction(SettingsAction.UpdateTheme(true))

            // When
            viewModel.onAction(SettingsAction.UpdateTheme(false))

            // Then
            val expected = SettingsUiState(
                selectedCurrency = Currency.USD,
                isDarkTheme = false,
            )
            assertEquals(expected, viewModel.state.value)
        }
}
