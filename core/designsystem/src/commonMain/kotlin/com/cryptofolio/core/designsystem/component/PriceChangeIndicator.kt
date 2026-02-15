package com.cryptofolio.core.designsystem.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.cryptofolio.core.designsystem.theme.CryptoFolioTheme

@Composable
fun PriceChangeIndicator(
    changePercentage: Double,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
) {
    val color = if (changePercentage >= 0) {
        CryptoFolioTheme.extendedColors.positive
    } else {
        CryptoFolioTheme.extendedColors.negative
    }
    val sign = if (changePercentage >= 0) "+" else ""
    val rounded = (kotlin.math.round(changePercentage * 100) / 100).toString()
    val formattedChange = "$sign$rounded%"

    Text(
        text = formattedChange,
        color = color,
        style = style,
        modifier = modifier,
    )
}
