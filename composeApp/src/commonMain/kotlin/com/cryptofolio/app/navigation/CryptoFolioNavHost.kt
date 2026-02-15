package com.cryptofolio.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cryptofolio.feature.assetdetail.AssetDetailScreen
import com.cryptofolio.feature.assetdetail.navigation.AssetDetailRoute
import com.cryptofolio.feature.portfolio.navigation.PortfolioRoute
import com.cryptofolio.feature.portfolio.overview.PortfolioScreen
import com.cryptofolio.feature.settings.SettingsScreen
import com.cryptofolio.feature.settings.navigation.SettingsRoute
import com.cryptofolio.feature.transaction.add.AddTransactionScreen
import com.cryptofolio.feature.transaction.detail.TransactionDetailScreen
import com.cryptofolio.feature.transaction.list.TransactionListScreen
import com.cryptofolio.feature.transaction.navigation.AddTransactionRoute
import com.cryptofolio.feature.transaction.navigation.TransactionDetailRoute
import com.cryptofolio.feature.transaction.navigation.TransactionListRoute

@Composable
fun CryptoFolioNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = PortfolioRoute,
        modifier = modifier,
    ) {
        composable<PortfolioRoute> {
            PortfolioScreen(
                onNavigateToAssetDetail = { coinId ->
                    navController.navigate(AssetDetailRoute(coinId))
                },
            )
        }

        composable<TransactionListRoute> {
            TransactionListScreen(
                onNavigateToAdd = { navController.navigate(AddTransactionRoute) },
                onNavigateToDetail = { id -> navController.navigate(TransactionDetailRoute(id)) },
            )
        }

        composable<AddTransactionRoute> {
            AddTransactionScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<TransactionDetailRoute> {
            TransactionDetailScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable<AssetDetailRoute> {
            AssetDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTransaction = { id -> navController.navigate(TransactionDetailRoute(id)) },
            )
        }

        composable<SettingsRoute> {
            SettingsScreen()
        }
    }
}
