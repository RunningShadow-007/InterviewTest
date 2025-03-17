package com.crypto.interview.feature.settings.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.crypto.interview.feature.settings.ui.SettingScreen
import kotlinx.serialization.Serializable

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 15:04<br>
 * Desc: <br>
 */

@Serializable
data object SettingScreenRoute // route to Setting screen

fun NavController.navigateToSettingScreen(navOptions: NavOptions? = null) =
    navigate(route = SettingScreenRoute, navOptions)

/**
 *  The Setting Screen of the app.
 *
 */
fun NavGraphBuilder.settingScreen(
    navController: NavController,
) {
    Log.d("NavigationDebug", "CryptoNavHost settingScreen: ${navController.currentDestination}")

    composable<SettingScreenRoute>(
    ) {
        SettingScreen(navController)
    }
}