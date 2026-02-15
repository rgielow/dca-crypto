package com.cryptofolio.feature.portfolio.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptofolio.core.designsystem.component.CryptoFolioCard
import com.cryptofolio.core.designsystem.component.PriceChangeIndicator
import com.cryptofolio.core.ui.CurrencyFormatter
import com.cryptofolio.domain.model.Asset

@Composable
fun AssetListItem(
    asset: Asset,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CryptoFolioCard(onClick = onClick, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${asset.coinName} (${asset.coinSymbol.uppercase()})",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${asset.totalAmount} ${asset.coinSymbol.uppercase()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.format(asset.currentValue, asset.currency),
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = CurrencyFormatter.format(asset.profitLoss, asset.currency, showSign = true),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    PriceChangeIndicator(
                        changePercentage = asset.profitLossPercentage,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
