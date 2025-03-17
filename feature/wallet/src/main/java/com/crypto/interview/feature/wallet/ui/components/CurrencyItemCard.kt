package com.crypto.interview.feature.wallet.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.interview.core.common.maths.toDoubleWithPrecision
import com.crypto.interview.core.ui.CardBackgroundColor
import com.crypto.interview.core.ui.component.SmartAsyncImage
import com.crypto.interview.feature.wallet.ui.theme.CardTextLightGrayColor
import java.text.NumberFormat
import java.util.Locale

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/17 11:57<br>
 * Desc: <br>
 */
private val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

@Composable
fun CurrencyItemCard(
    name: String,
    code: String,
    amount: String,
    usdValue: String,
    imageUrl: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(vertical = 3.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 货币图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Log.d("UIDebug", "imageUrl: $imageUrl")
                SmartAsyncImage(
                    imageUrl = imageUrl,
                    contentDescription = name,
                    modifier = Modifier.size(40.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 货币名称
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            // 美元价值
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "$amount $code",
                    fontSize = 16.sp,
                )
                Text(
                    text = formatter.format(usdValue.toDoubleWithPrecision()),
                    fontSize = 14.sp,
                    color = CardTextLightGrayColor
                )
            }
        }
    }
}