package com.crypto.interview.feature.wallet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.crypto.interview.feature.wallet.R

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 11:59<br>
 * Desc: <br>
 */
@Composable
internal fun ConstraintLayoutScope.TopToolsBar(
    topSpacer: ConstrainedLayoutReference,
    settingId: ConstrainedLayoutReference,
    scanId: ConstrainedLayoutReference
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .fillMaxWidth()
            .constrainAs(topSpacer) {
                top.linkTo(parent.top, 20.dp)
            }
    )
    Image(
        painter = painterResource(id = R.drawable.wallet_ic_setting),
        contentDescription = "Setting",
        modifier = Modifier
            .constrainAs(ref = settingId) {
                top.linkTo(topSpacer.bottom)
                start.linkTo(parent.start, 16.dp)
            }
            .size(32.dp)
    )

    Image(
        painter = painterResource(id = R.drawable.wallet_ic_scan),
        contentDescription = "Scan",
        modifier = Modifier.constrainAs(ref = scanId) {
            top.linkTo(settingId.top)
            end.linkTo(parent.end, 8.dp)
        })
}