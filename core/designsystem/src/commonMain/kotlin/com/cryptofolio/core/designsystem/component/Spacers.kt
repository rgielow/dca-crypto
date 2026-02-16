package com.cryptofolio.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.cryptofolio.core.designsystem.theme.Spacing

@Composable
fun VerticalSpacer(height: Dp = Spacing.md) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalSpacer(width: Dp = Spacing.md) {
    Spacer(modifier = Modifier.width(width))
}
