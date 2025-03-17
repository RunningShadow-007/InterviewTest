package com.crypto.interview.defi.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crypto.interview.defi.ui.component.DeFiCard

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/15 23:27<br>
 * Desc: <br>
 */
@Composable
 fun DeFiScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp).background(Color.Transparent))
        DeFiCard(title = "质押", description = "质押您的加密货币以赚取收益")

        Spacer(modifier = Modifier.height(6.dp))

        DeFiCard(title = "借贷", description = "使用您的加密货币作为抵押获取贷款")

        Spacer(modifier = Modifier.height(6.dp))

        DeFiCard(title = "流动性挖矿", description = "提供流动性以赚取交易费用和奖励")
    }
}