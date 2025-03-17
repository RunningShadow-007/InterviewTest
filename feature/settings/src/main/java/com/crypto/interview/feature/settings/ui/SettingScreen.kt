package com.crypto.interview.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 14:53<br>
 * Desc: <br>
 */
@Composable
fun SettingScreen(navController: NavController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (backButtonId, contentId) = createRefs()
        Icon(
            modifier = Modifier
                .clickable {
                    navController.navigateUp()
                }
                .size(36.dp)
                .constrainAs(backButtonId) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top, 40.dp)
                },
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )

        Text("This is Setting Page", modifier = Modifier.constrainAs(contentId) {
            centerTo(parent)
        })
    }
}