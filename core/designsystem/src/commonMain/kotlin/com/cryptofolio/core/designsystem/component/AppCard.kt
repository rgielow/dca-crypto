package com.cryptofolio.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cryptofolio.core.designsystem.theme.Spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.xxs),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            shape = MaterialTheme.shapes.medium,
            content = content,
        )
    } else {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.xxs),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            shape = MaterialTheme.shapes.medium,
            content = content,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppPreview {
        AppCard(modifier = Modifier.padding(all = Spacing.sm)) {
            Column { AppButton(text = "Preview", onClick = {}) }
        }
    }
}