package com.crypto.interview.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 07:10<br>
 * Desc: <br>
 */

val OnBackgroundNight = Color.Black.copy(alpha = 0.7f)
val BackgroundNight=Color.Black
val Background=Color(0xFFf2f9fe)
val OnBackground = Color(0xFFFFFFFF)
val Gray = Color.Gray.copy(alpha = 0.7f)
val FrenchGray = Color.Gray.copy(alpha = 0.3f)


val ScreenBackgroundColor: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            BackgroundNight
        }else{
            Background
        }
    }
val CardBackgroundColor:Color
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            OnBackgroundNight
        }else{
            OnBackground
        }
    }