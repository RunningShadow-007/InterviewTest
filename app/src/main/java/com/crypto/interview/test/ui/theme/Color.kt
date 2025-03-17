package com.crypto.interview.test.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val OnBackground = Color(0xFFFFFFFF)
val Background=Color(0xFFf2f9fe)

val Gray = Color.Gray.copy(alpha = 0.7f)
val FrenchGray = Color.Gray.copy(alpha = 0.3f)
val OnBackgroundNight = Color.Black.copy(alpha = 0.7f)
val BackgroundNight=Color.Black

val TabItemSelectedColorLight=Color(0xFF408df1)
val TabItemUnselectedColorLight=Gray
val TabItemSelectedColorDark=Color(0xFF408df1)
val TabItemUnselectedColorDark=FrenchGray

val TabItemUnselectedColor: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            TabItemUnselectedColorDark
        }else{
            TabItemUnselectedColorLight
        }
    }
val TabItemSelectedColor: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            TabItemSelectedColorDark
        }else{
            TabItemSelectedColorLight
        }
    }

val TabBarBackgroundColor: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            OnBackgroundNight
        }else{
            OnBackground
        }
    }

val MainScreenBackgroundColor: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            BackgroundNight
        }else{
            Background
        }
    }




