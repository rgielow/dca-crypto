package com.cryptofolio.feature.transaction.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptofolio.core.designsystem.component.ErrorView
import com.cryptofolio.core.designsystem.component.LoadingIndicator
import com.cryptofolio.core.designsystem.icon.CryptoFolioIcons
import com.cryptofolio.core.ui.CollectAsEffect
import com.cryptofolio.feature.transaction.component.TransactionCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: TransactionListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectAsEffect(viewModel.events) { event ->
        when (event) {
            TransactionListEvent.NavigateToAddTransaction -> onNavigateToAdd()
            is TransactionListEvent.NavigateToTransactionDetail -> onNavigateToDetail(event.id)
            is TransactionListEvent.ShowError -> {}
            TransactionListEvent.TransactionDeleted -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Transactions") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAction(TransactionListAction.AddTransaction) },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = CryptoFolioIcons.Add,
                    contentDescription = "Add Transaction",
                )
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorView(
                    message = state.error!!,
                    onRetry = { viewModel.onAction(TransactionListAction.LoadTransactions) },
                )
                state.transactions.isEmpty() -> {
                    Text(
                        text = "No transactions yet.\nTap + to add your first transaction.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(state.transactions, key = { it.id }) { transaction ->
                            TransactionCard(
                                transaction = transaction,
                                onClick = { viewModel.onAction(TransactionListAction.SelectTransaction(transaction.id)) },
                                onDelete = { viewModel.onAction(TransactionListAction.DeleteTransaction(transaction.id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}
