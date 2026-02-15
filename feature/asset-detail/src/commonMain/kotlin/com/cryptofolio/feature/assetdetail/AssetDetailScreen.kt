package com.cryptofolio.feature.assetdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.cryptofolio.core.designsystem.component.CryptoFolioCard
import com.cryptofolio.core.designsystem.component.ErrorView
import com.cryptofolio.core.designsystem.component.LoadingIndicator
import com.cryptofolio.core.designsystem.component.PriceChangeIndicator
import com.cryptofolio.core.designsystem.icon.CryptoFolioIcons
import com.cryptofolio.core.ui.CollectAsEffect
import com.cryptofolio.core.ui.CurrencyFormatter
import com.cryptofolio.core.ui.DateFormatter
import com.cryptofolio.domain.model.Transaction
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTransaction: (Long) -> Unit,
    viewModel: AssetDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectAsEffect(viewModel.events) { event ->
        when (event) {
            AssetDetailEvent.NavigateBack -> onNavigateBack()
            is AssetDetailEvent.NavigateToTransaction -> onNavigateToTransaction(event.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.asset?.coinName ?: "Asset Detail") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onAction(AssetDetailAction.NavigateBack) }) {
                        Icon(CryptoFolioIcons.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorView(message = state.error!!)
            state.asset != null -> {
                val asset = state.asset!!
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        CryptoFolioCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${asset.coinName} (${asset.coinSymbol.uppercase()})",
                                    style = MaterialTheme.typography.headlineMedium,
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Column {
                                        Text("Holdings", style = MaterialTheme.typography.bodySmall)
                                        Text(
                                            text = CurrencyFormatter.format(asset.currentValue, asset.currency),
                                            style = MaterialTheme.typography.titleLarge,
                                        )
                                    }
                                    Column {
                                        Text("P&L", style = MaterialTheme.typography.bodySmall)
                                        Text(
                                            text = CurrencyFormatter.format(asset.profitLoss, asset.currency, showSign = true),
                                            style = MaterialTheme.typography.titleLarge,
                                        )
                                        PriceChangeIndicator(changePercentage = asset.profitLossPercentage)
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    DetailItem("Amount", "${asset.totalAmount} ${asset.coinSymbol.uppercase()}")
                                    DetailItem("Avg Price", CurrencyFormatter.format(asset.averageBuyPrice, asset.currency))
                                    DetailItem("Invested", CurrencyFormatter.format(asset.totalInvested, asset.currency))
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Transaction History",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }

                    items(state.transactions, key = { it.id }) { tx ->
                        TransactionHistoryItem(
                            transaction = tx,
                            onClick = { viewModel.onAction(AssetDetailAction.SelectTransaction(tx.id)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun TransactionHistoryItem(transaction: Transaction, onClick: () -> Unit) {
    CryptoFolioCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = "${transaction.type.name} - ${transaction.exchange.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = DateFormatter.formatDate(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column {
                Text(
                    text = "${transaction.amount} ${transaction.coinSymbol}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = CurrencyFormatter.format(transaction.totalCost, transaction.currency),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}
