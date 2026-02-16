package com.cryptofolio.feature.transaction.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptofolio.core.designsystem.component.AppButton
import com.cryptofolio.core.designsystem.component.AppCard
import com.cryptofolio.core.designsystem.component.ErrorView
import com.cryptofolio.core.designsystem.component.LoadingIndicator
import com.cryptofolio.core.designsystem.icon.CryptoFolioIcons
import com.cryptofolio.core.ui.CollectAsEffect
import com.cryptofolio.core.ui.CurrencyFormatter
import com.cryptofolio.core.ui.DateFormatter
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: TransactionDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectAsEffect(viewModel.events) { event ->
        when (event) {
            TransactionDetailEvent.NavigateBack -> onNavigateBack()
            is TransactionDetailEvent.ShowError -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(CryptoFolioIcons.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorView(message = state.error!!)
            state.transaction != null -> {
                val tx = state.transaction!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AppCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${tx.coinName} (${tx.coinSymbol})",
                                style = MaterialTheme.typography.headlineMedium,
                            )
                            Spacer(Modifier.height(8.dp))
                            DetailRow("Type", tx.type.name)
                            DetailRow("Amount", "${tx.amount} ${tx.coinSymbol}")
                            DetailRow("Price", CurrencyFormatter.format(tx.pricePerUnit, tx.currency))
                            DetailRow("Total", CurrencyFormatter.format(tx.totalCost, tx.currency))
                            if (tx.fee > 0) DetailRow("Fee", CurrencyFormatter.format(tx.fee, tx.currency))
                            DetailRow("Exchange", tx.exchange.displayName)
                            DetailRow("Date", DateFormatter.formatDateTime(tx.date))
                            if (tx.notes.isNotBlank()) DetailRow("Notes", tx.notes)
                        }
                    }

                    AppButton(
                        text = "Delete Transaction",
                        onClick = { viewModel.onAction(TransactionDetailAction.Delete) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
