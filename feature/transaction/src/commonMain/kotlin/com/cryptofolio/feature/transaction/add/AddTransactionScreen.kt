package com.cryptofolio.feature.transaction.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptofolio.core.designsystem.component.AppButton
import com.cryptofolio.core.designsystem.component.AppTextField
import com.cryptofolio.core.designsystem.icon.CryptoFolioIcons
import com.cryptofolio.core.ui.CollectAsEffect
import com.cryptofolio.domain.model.Currency
import com.cryptofolio.domain.model.TransactionType
import com.cryptofolio.feature.transaction.component.ExchangePicker
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddTransactionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectAsEffect(viewModel.events) { event ->
        when (event) {
            AddTransactionEvent.TransactionSaved -> onNavigateBack()
            is AddTransactionEvent.ShowError -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(CryptoFolioIcons.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TransactionType.entries.forEach { type ->
                    FilterChip(
                        selected = state.type == type,
                        onClick = { viewModel.onAction(AddTransactionAction.UpdateType(type)) },
                        label = { Text(type.name) },
                    )
                }
            }

            AppTextField(
                value = state.coinName,
                onValueChange = {
                    viewModel.onAction(AddTransactionAction.UpdateCoinName(it))
                    viewModel.onAction(AddTransactionAction.UpdateCoinId(it.lowercase().replace(" ", "-")))
                },
                label = "Coin Name",
                isError = state.coinError != null,
                errorMessage = state.coinError,
            )

            AppTextField(
                value = state.coinSymbol,
                onValueChange = { viewModel.onAction(AddTransactionAction.UpdateCoinSymbol(it)) },
                label = "Symbol (e.g. BTC)",
            )

            AppTextField(
                value = state.amount,
                onValueChange = { viewModel.onAction(AddTransactionAction.UpdateAmount(it)) },
                label = "Amount",
                isError = state.amountError != null,
                errorMessage = state.amountError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            AppTextField(
                value = state.pricePerUnit,
                onValueChange = { viewModel.onAction(AddTransactionAction.UpdatePrice(it)) },
                label = "Price per Unit",
                isError = state.priceError != null,
                errorMessage = state.priceError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            AppTextField(
                value = state.fee,
                onValueChange = { viewModel.onAction(AddTransactionAction.UpdateFee(it)) },
                label = "Fee (optional)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            ExchangePicker(
                selected = state.exchange,
                onSelected = { viewModel.onAction(AddTransactionAction.UpdateExchange(it)) },
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Currency.entries.forEach { currency ->
                    FilterChip(
                        selected = state.currency == currency,
                        onClick = { viewModel.onAction(AddTransactionAction.UpdateCurrency(currency)) },
                        label = { Text("${currency.symbol} ${currency.code.uppercase()}") },
                    )
                }
            }

            AppTextField(
                value = state.notes,
                onValueChange = { viewModel.onAction(AddTransactionAction.UpdateNotes(it)) },
                label = "Notes (optional)",
                singleLine = false,
            )

            Spacer(Modifier.height(8.dp))

            AppButton(
                text = "Save Transaction",
                onClick = { viewModel.onAction(AddTransactionAction.Save) },
                enabled = !state.isLoading,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
