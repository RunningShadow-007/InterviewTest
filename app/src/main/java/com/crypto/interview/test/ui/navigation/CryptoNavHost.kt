package com.crypto.interview.test.ui.navigation

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 18:44<br>
 * Desc:Top-level navigation graph.  <br>
 */
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.crypto.interview.feature.wallet.navigation.WalletScreenRoute
import com.crypto.interview.feature.wallet.navigation.walletScreen
import com.crypto.interview.test.ui.screen.MainScreen
import com.crypto.interview.test.ui.screen.MainScreenRoute

@Composable
fun CryptoNavHost(
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()
    // 添加状态监听
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("NavigationDebug", "Current destination updated: ${destination?.route}")
        }
    }
    NavHost(
        navController = navController,
        startDestination = MainScreenRoute,
        modifier = modifier,
    ) {
            Log.d("NavigationDebug", "CryptoNavHost created with start destination: ${navController.currentDestination}")
            composable<MainScreenRoute>() {
                DisposableEffect(Unit) {
                    Log.d("NavigationDebug", "MainScreen destination activated")
                    onDispose { /* 清理操作 */ }
                }
                MainScreen(navController = navController)
            }

    }
}