package com.crypto.interview.defi.navigation

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 04:37<br>
 * Desc: <br>
 */

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crypto.interview.defi.ui.screen.DeFiScreen
import kotlinx.serialization.Serializable

@Serializable
data object DeFiScreenRoute // route to Wallet screen

fun NavController.navigateToDeFiScreen(navOptions: NavOptions? = null) =
    navigate(route = DeFiScreenRoute, navOptions)

/**
 *  The DeFi Screen of the app.
 *
 */
fun NavGraphBuilder.defiScreen(
    navController: NavController,
) {
    Log.d("NavigationDebug", "CryptoNavHost defiScreen: ${navController.currentDestination}")

    composable<DeFiScreenRoute>(
    ) {
        DeFiScreen()
    }
}
