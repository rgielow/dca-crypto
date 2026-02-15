package com.cryptofolio.feature.settings

import com.cryptofolio.core.ui.BaseViewModel

class SettingsViewModel : BaseViewModel<SettingsUiState, SettingsAction, SettingsEvent>(
    SettingsUiState()
) {

    override fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.UpdateCurrency -> {
                updateState { copy(selectedCurrency = action.currency) }
                sendEvent(SettingsEvent.ShowMessage("Currency updated"))
            }
            is SettingsAction.UpdateTheme -> {
                updateState { copy(isDarkTheme = action.isDark) }
            }
        }
    }
}
