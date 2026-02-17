package com.cryptofolio.feature.settings

import com.cryptofolio.domain.model.Currency

sealed interface SettingsAction {
    data class UpdateCurrency(val currency: Currency) : SettingsAction
    data class UpdateTheme(val isDark: Boolean) : SettingsAction
}
