package com.crypto.interview.feature.wallet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.crypto.interview.feature.wallet.R
import com.crypto.interview.feature.wallet.ui.theme.CardTextWhiteColor

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 11:56<br>
 * Desc: <br>
 */
@Composable
internal fun ConstraintLayoutScope.OperatorsCard(
    sendId: ConstrainedLayoutReference,
    totalBalanceId: ConstrainedLayoutReference,
    receiveId: ConstrainedLayoutReference,
    horizontalDividerId: ConstrainedLayoutReference,
    brandDescId: ConstrainedLayoutReference
) {
    Column(modifier = Modifier.constrainAs(ref = sendId) {
        start.linkTo(parent.start, margin = 80.dp)
        top.linkTo(totalBalanceId.bottom, 32.dp)
    }, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painterResource(id = R.drawable.wallet_ic_send),
            contentDescription = "Send",
            modifier = Modifier.size(50.dp)
        )
        Text(text = "Send", fontSize = 16.sp, color = CardTextWhiteColor)
    }

    Column(modifier = Modifier.constrainAs(ref = receiveId) {
        end.linkTo(parent.end, margin = 80.dp)
        top.linkTo(totalBalanceId.bottom, 32.dp)
    }, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painterResource(id = R.drawable.wallet_ic_receive),
            contentDescription = "Receive",
            modifier = Modifier.size(50.dp)
        )
        Text(text = "Receive", fontSize = 16.sp, color = CardTextWhiteColor)
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.Transparent,
        modifier = Modifier
            .height(1.dp)
            .constrainAs(horizontalDividerId) {
                top.linkTo(brandDescId.bottom, 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
}