package com.crypto.interview.test.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.crypto.interview.test.R
import com.crypto.interview.test.ui.theme.TabItemSelectedColor
import com.crypto.interview.test.ui.theme.TabItemUnselectedColor

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/15 22:04<br>
 * Desc: <br>
 */

@Composable
private fun StatefulIcon(
    isSelected: Boolean,
    @DrawableRes drawableRes: Int,
    @StringRes contentDescription: Int? = null
) {
    Icon(
        imageVector = ImageVector.vectorResource(drawableRes),
        contentDescription = contentDescription?.let { stringResource(it) },
        tint = if (isSelected) TabItemSelectedColor else TabItemUnselectedColor
    )
}

@Composable
internal fun RowScope.NavBarItem(
    index: Int,
    selectedIndex: Int,
    @DrawableRes icon: Int,
    @StringRes label: Int,
    onItemClicked: (Int) -> Unit
) {
    NavigationBarItem(
        modifier = Modifier.testTag("MainScreen-NavBarItem$index"),
        selected = selectedIndex == index,
        onClick = {
            onItemClicked(index)
        },
        icon = {
            StatefulIcon(selectedIndex == index, icon, label)
        },
        label = { Text(stringResource(label)) },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent
        )
    )
}

enum class NavItemArray(@DrawableRes val icon: Int, @StringRes val label: Int) {
    NAV_WALLET(R.drawable.ic_wallet, R.string.wallet), NAV_DEFI(R.drawable.ic_defi, R.string.defi)
}
