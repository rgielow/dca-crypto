package com.cryptofolio.feature.settings

import com.cryptofolio.domain.model.Currency

data class SettingsUiState(
    val selectedCurrency: Currency = Currency.USD,
    val isDarkTheme: Boolean = false,
)
