package com.crypto.interview.test.ui.navigation

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 18:44<br>
 * Desc:Top-level navigation graph.  <br>
 */
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crypto.interview.feature.settings.navigation.settingScreen
import com.crypto.interview.test.ui.screen.MainScreen
import com.crypto.interview.test.ui.screen.MainScreenRoute

@Composable
fun CryptoNavHost(
    modifier: Modifier = Modifier,
) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MainScreenRoute,
        modifier = modifier,
    ) {
        composable<MainScreenRoute>() {
            MainScreen(navController = navController)
        }
        settingScreen(navController)
    }
}