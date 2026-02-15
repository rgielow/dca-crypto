package com.cryptofolio.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cryptofolio.app.navigation.CryptoFolioNavHost
import com.cryptofolio.core.designsystem.icon.CryptoFolioIcons
import com.cryptofolio.core.designsystem.theme.CryptoFolioTheme
import com.cryptofolio.feature.portfolio.navigation.PortfolioRoute
import com.cryptofolio.feature.settings.navigation.SettingsRoute
import com.cryptofolio.feature.transaction.navigation.TransactionListRoute

@Composable
fun App() {
    CryptoFolioTheme {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry?.destination

        val showBottomBar = currentDestination?.let { dest ->
            dest.hasRoute<PortfolioRoute>() ||
                dest.hasRoute<TransactionListRoute>() ||
                dest.hasRoute<SettingsRoute>()
        } ?: true

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentDestination?.hasRoute<PortfolioRoute>() == true,
                            onClick = {
                                navController.navigate(PortfolioRoute) {
                                    popUpTo(PortfolioRoute) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentDestination?.hasRoute<PortfolioRoute>() == true)
                                        CryptoFolioIcons.Home else CryptoFolioIcons.HomeOutlined,
                                    contentDescription = "Portfolio",
                                )
                            },
                            label = { Text("Portfolio") },
                        )
                        NavigationBarItem(
                            selected = currentDestination?.hasRoute<TransactionListRoute>() == true,
                            onClick = {
                                navController.navigate(TransactionListRoute) {
                                    popUpTo(PortfolioRoute)
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = CryptoFolioIcons.Add,
                                    contentDescription = "Transactions",
                                )
                            },
                            label = { Text("Transactions") },
                        )
                        NavigationBarItem(
                            selected = currentDestination?.hasRoute<SettingsRoute>() == true,
                            onClick = {
                                navController.navigate(SettingsRoute) {
                                    popUpTo(PortfolioRoute)
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentDestination?.hasRoute<SettingsRoute>() == true)
                                        CryptoFolioIcons.Settings else CryptoFolioIcons.SettingsOutlined,
                                    contentDescription = "Settings",
                                )
                            },
                            label = { Text("Settings") },
                        )
                    }
                }
            },
        ) { padding ->
            CryptoFolioNavHost(
                navController = navController,
                modifier = Modifier.padding(padding),
            )
        }
    }
}
