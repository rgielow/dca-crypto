package com.cryptofolio.feature.portfolio.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptofolio.core.designsystem.component.AppCard
import com.cryptofolio.core.designsystem.component.PriceChangeIndicator
import com.cryptofolio.core.ui.CurrencyFormatter
import com.cryptofolio.domain.model.Portfolio

@Composable
fun PortfolioSummaryCard(
    portfolio: Portfolio,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Value",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = CurrencyFormatter.format(portfolio.totalCurrentValue, portfolio.currency),
                style = MaterialTheme.typography.headlineLarge,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column {
                    Text("Invested", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = CurrencyFormatter.format(portfolio.totalInvested, portfolio.currency),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Column {
                    Text("P&L", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = CurrencyFormatter.format(portfolio.totalProfitLoss, portfolio.currency, showSign = true),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                PriceChangeIndicator(
                    changePercentage = portfolio.totalProfitLossPercentage,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
