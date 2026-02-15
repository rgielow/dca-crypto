package com.cryptofolio.feature.portfolio.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptofolio.core.designsystem.component.ErrorView
import com.cryptofolio.core.designsystem.component.LoadingIndicator
import com.cryptofolio.core.ui.CollectAsEffect
import com.cryptofolio.feature.portfolio.component.AssetListItem
import com.cryptofolio.feature.portfolio.component.PortfolioSummaryCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    onNavigateToAssetDetail: (String) -> Unit,
    viewModel: PortfolioViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectAsEffect(viewModel.events) { event ->
        when (event) {
            is PortfolioEvent.NavigateToAssetDetail -> onNavigateToAssetDetail(event.coinId)
            is PortfolioEvent.ShowError -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Portfolio") })
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorView(
                    message = state.error!!,
                    onRetry = { viewModel.onAction(PortfolioAction.LoadPortfolio) },
                )
                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.onAction(PortfolioAction.RefreshPrices) },
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            item {
                                PortfolioSummaryCard(portfolio = state.portfolio)
                            }
                            items(state.portfolio.assets, key = { it.coinId }) { asset ->
                                AssetListItem(
                                    asset = asset,
                                    onClick = { viewModel.onAction(PortfolioAction.SelectAsset(asset.coinId)) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
