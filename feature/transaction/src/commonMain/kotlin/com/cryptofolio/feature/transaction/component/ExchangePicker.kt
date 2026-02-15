package com.cryptofolio.feature.transaction.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cryptofolio.domain.model.Exchange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExchangePicker(
    selected: Exchange,
    onSelected: (Exchange) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Exchange.entries.forEach { exchange ->
            FilterChip(
                selected = selected == exchange,
                onClick = { onSelected(exchange) },
                label = { Text(exchange.displayName) },
            )
        }
    }
}
