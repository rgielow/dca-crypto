package com.cryptofolio.feature.portfolio.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cryptofolio.core.designsystem.component.PriceChangeIndicator
import com.cryptofolio.core.ui.CurrencyFormatter
import com.cryptofolio.domain.model.Currency

@Composable
fun PnlDisplay(
    profitLoss: Double,
    profitLossPercentage: Double,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Text(
            text = CurrencyFormatter.format(profitLoss, currency, showSign = true),
            style = MaterialTheme.typography.titleMedium,
        )
        PriceChangeIndicator(
            changePercentage = profitLossPercentage,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
