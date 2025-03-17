package com.crypto.interview.feature.wallet.navigation


/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 18:59<br>
 * Desc: <br>
 */
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.crypto.interview.feature.wallet.ui.WalletScreen
import kotlinx.serialization.Serializable

@Serializable
data object WalletScreenRoute // route to Wallet screen

fun NavController.navigateToWalletScreen(navOptions: NavOptions? = null) =
    navigate(route = WalletScreenRoute, navOptions)

/**
 *  The Wallet section of the app. It can also display information about wallet balances.
 *
 *  @param onSettingClick - Called when Setting button is clicked
 *  @param settingDestination - Destination for setting screen
 */
fun NavGraphBuilder.walletScreen(
    navController: NavController,
    onSettingClick: (() -> Unit)? = null,
    settingDestination: NavGraphBuilder.() -> Unit,
) {
    Log.d("NavigationDebug", "CryptoNavHost walletScreen: ${navController.currentDestination}")

    composable<WalletScreenRoute>(
    ) {
        WalletScreen(onSettingClick = onSettingClick)
    }
    settingDestination()
}

