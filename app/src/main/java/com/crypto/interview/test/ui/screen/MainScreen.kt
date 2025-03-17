package com.crypto.interview.test.ui.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.crypto.interview.defi.ui.screen.DeFiScreen
import com.crypto.interview.feature.settings.navigation.SettingScreenRoute
import com.crypto.interview.feature.wallet.ui.WalletScreen
import com.crypto.interview.test.ui.components.NavBarItem
import com.crypto.interview.test.ui.components.NavItemArray
import com.crypto.interview.test.ui.theme.MainScreenBackgroundColor
import com.crypto.interview.test.ui.theme.TabBarBackgroundColor
import kotlinx.serialization.Serializable

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/15 23:29<br>
 * Desc: <br>
 */
@Serializable
data object MainScreenRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController = rememberNavController()) {

    var selectedIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(containerColor = TabBarBackgroundColor) {
                NavItemArray.entries.forEachIndexed { index, item ->
                    NavBarItem(
                        index = index,
                        selectedIndex = selectedIndex,
                        icon = item.icon,
                        label = item.label
                    ) {

                    selectedIndex = it
                    }
                }
            }
        }) { contentPadding ->
        Surface(
            color = MainScreenBackgroundColor,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            when (selectedIndex) {
                NavItemArray.NAV_WALLET.ordinal -> WalletScreen(onSettingClick = {
                    navController.navigate(SettingScreenRoute)
                })

                NavItemArray.NAV_DEFI.ordinal -> DeFiScreen()
            }
        }
    }
}