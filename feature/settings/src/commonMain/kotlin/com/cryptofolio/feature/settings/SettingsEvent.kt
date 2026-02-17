package com.cryptofolio.feature.settings

sealed interface SettingsEvent {
    data class ShowMessage(val message: String) : SettingsEvent
}
