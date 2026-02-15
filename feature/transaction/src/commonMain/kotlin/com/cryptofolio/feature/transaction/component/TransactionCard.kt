package com.cryptofolio.feature.transaction.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptofolio.core.designsystem.component.CryptoFolioCard
import com.cryptofolio.core.designsystem.icon.CryptoFolioIcons
import com.cryptofolio.core.designsystem.theme.CryptoFolioTheme
import com.cryptofolio.core.ui.CurrencyFormatter
import com.cryptofolio.core.ui.DateFormatter
import com.cryptofolio.domain.model.Transaction
import com.cryptofolio.domain.model.TransactionType

@Composable
fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val typeColor = if (transaction.type == TransactionType.BUY) {
        CryptoFolioTheme.extendedColors.positive
    } else {
        CryptoFolioTheme.extendedColors.negative
    }

    CryptoFolioCard(onClick = onClick, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = transaction.type.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = typeColor,
                    )
                    Text(
                        text = "${transaction.coinName} (${transaction.coinSymbol})",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                Text(
                    text = "${transaction.amount} @ ${CurrencyFormatter.format(transaction.pricePerUnit, transaction.currency)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${transaction.exchange.displayName} - ${DateFormatter.formatDate(transaction.date)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyFormatter.format(transaction.totalCost, transaction.currency),
                    style = MaterialTheme.typography.titleMedium,
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = CryptoFolioIcons.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
