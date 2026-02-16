package com.cryptofolio.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.cryptofolio.core.designsystem.theme.AppTheme

@Composable
fun AppPreview(content: @Composable () -> Unit) {
    Column {
        AppTheme(darkTheme = false) {
            Surface { content() }
        }
        AppTheme(darkTheme = true) {
            Surface { content() }
        }
    }
}
