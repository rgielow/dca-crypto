package com.cryptofolio.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.cryptofolio.core.designsystem.theme.ExtendedTheme
import com.cryptofolio.core.designsystem.theme.Spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val ONE_HUNDRED = 100
private const val ZERO = 0
private const val EMPTY_STRING = ""
private const val PLUS_SIGNAL = "+"

@Composable
fun PriceChangeIndicator(
    changePercentage: Double,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
) {
    val color = if (changePercentage >= ZERO) {
        ExtendedTheme.extendedColors.positive
    } else {
        ExtendedTheme.extendedColors.negative
    }
    val sign = if (changePercentage >= ZERO) PLUS_SIGNAL else EMPTY_STRING
    val rounded = (kotlin.math.round(changePercentage * ONE_HUNDRED) / ONE_HUNDRED).toString()
    val formattedChange = "$sign$rounded%"

    Text(
        text = formattedChange,
        color = color,
        style = style,
        modifier = modifier,
    )
}


@Preview
@Composable
private fun PriceChangeIndicatorPreview() {
    Row {
        AppPreview {
            PriceChangeIndicator(changePercentage = 0.06)
        }
        HorizontalSpacer(width = Spacing.xxs)
        AppPreview {
            PriceChangeIndicator(changePercentage = -0.06)
        }
    }
}