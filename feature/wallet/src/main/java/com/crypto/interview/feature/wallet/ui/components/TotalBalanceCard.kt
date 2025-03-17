package com.crypto.interview.feature.wallet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.crypto.interview.core.domain.model.WalletData
import com.crypto.interview.feature.wallet.R
import com.crypto.interview.feature.wallet.ui.theme.CardTextGrayColor
import com.crypto.interview.feature.wallet.ui.theme.CardTextLightGrayColor
import com.crypto.interview.feature.wallet.ui.theme.CardTextWhiteColor

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 11:56<br>
 * Desc: <br>
 */
@Composable
internal fun ConstraintLayoutScope.TotalBalanceCard(
    brandDescId: ConstrainedLayoutReference,
    settingId: ConstrainedLayoutReference,
    totalBalanceId: ConstrainedLayoutReference,
    walletData: WalletData
) {
    Row(
        modifier = Modifier
            .constrainAs(ref = brandDescId) {
                top.linkTo(settingId.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.wallet_ic_logo),
            contentDescription = "Brand",
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "crypto.com",
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = CardTextWhiteColor
        )
        VerticalDivider(
            modifier = Modifier
                .height(32.dp)
                .padding(horizontal = 5.dp),
            thickness = 1.dp,
            color = CardTextLightGrayColor
        )
        Text(
            text = "DEFI WALLET",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = CardTextLightGrayColor
        )
    }

    Row(
        modifier = Modifier
            .constrainAs(ref = totalBalanceId) {
                top.linkTo(brandDescId.bottom, 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(text = buildAnnotatedString() {
            withStyle(SpanStyle(color = CardTextGrayColor, fontSize = 22.sp)) {
                append("$ ")
            }
            withStyle(
                SpanStyle(
                    color = CardTextWhiteColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(walletData.totalUsdValue.toString())
            }
            withStyle(
                SpanStyle(
                    color = CardTextGrayColor,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" USD")
            }
        })
    }
}