package com.cryptofolio.feature.settings

import com.cryptofolio.domain.model.Currency

data class SettingsUiState(
    val selectedCurrency: Currency = Currency.USD,
    val isDarkTheme: Boolean = false,
)

sealed interface SettingsAction {
    data class UpdateCurrency(val currency: Currency) : SettingsAction
    data class UpdateTheme(val isDark: Boolean) : SettingsAction
}

sealed interface SettingsEvent {
    data class ShowMessage(val message: String) : SettingsEvent
}
