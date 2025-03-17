package com.crypto.interview.feature.wallet.ui

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:01<br>
 * Desc: <br>
 */
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
//import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
import com.crypto.interview.core.domain.model.WalletData
import com.crypto.interview.core.ui.Background
import com.crypto.interview.core.ui.CardBackgroundColor
import com.crypto.interview.core.ui.ScreenBackgroundColor
import com.crypto.interview.core.ui.component.SmartAsyncImage
import com.crypto.interview.feature.wallet.R
import com.crypto.interview.feature.wallet.ui.theme.CardTextGrayColor
import com.crypto.interview.feature.wallet.ui.theme.CardTextLightGrayColor
import com.crypto.interview.feature.wallet.ui.theme.CardTextWhiteColor
import com.crypto.interview.feature.wallet.ui.theme.WalletCardBackgroundColor
import com.crypto.interview.feature.wallet.viewmodel.WalletUiState
import com.crypto.interview.feature.wallet.viewmodel.WalletViewModel
import java.text.NumberFormat
import java.util.ArrayList
import java.util.Locale
import kotlin.math.tan

@Composable
fun WalletScreen(onSettingClick: (() -> Unit)? = null, onScanClick: ((() -> Unit))? = null) {
    val viewModel: WalletViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WalletViewModel() as T
            }
        }
    )

    val walletState by viewModel.walletUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (walletState) {
            is WalletUiState.Loading -> {
                CircularProgressIndicator()
            }

            is WalletUiState.Success -> {
                val walletData = (walletState as WalletUiState.Success).walletData
                WalletContent(walletData)
            }

            is WalletUiState.Failed -> {
                Text(text = (walletState as WalletUiState.Failed).error)
            }
        }

    }
}

@Preview
@Composable
internal fun WalletContent(walletData: WalletData=WalletData(totalUsdValue = 10000.0, walletBalances = ArrayList(10) )) {
    ConstraintLayout(modifier = Modifier.fillMaxSize().background(color=WalletCardBackgroundColor)) {
        val (topSpacer,settingId, scanId, brandDescId, totalBalanceId, sendId, receiveId, horizontalDividerId, currencyListId) = createRefs()
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .fillMaxWidth()
            .constrainAs(topSpacer) {
                top.linkTo(parent.top,20.dp)
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

        TotalBalanceCard(brandDescId, settingId, totalBalanceId, walletData)

        OperatorsCard(sendId, totalBalanceId, receiveId)
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Transparent,
            modifier = Modifier.height(1.dp).constrainAs(horizontalDividerId) {
                top.linkTo(brandDescId.bottom, 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        LazyColumn(
            modifier = Modifier
                .background(color = CardBackgroundColor, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .fillMaxHeight(0.55f)
                .padding(top = 16.dp)
                .constrainAs(currencyListId) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            items(
                walletData.walletBalances.size,
                key = { index -> walletData.walletBalances[index].currency.coinId })
            { index ->
                val currency = walletData.walletBalances[index].currency
                val balance = walletData.walletBalances[index].balance
                val usdValue = walletData.walletBalances[index].usdValue
                CurrencyItem(
                    name = currency.name,
                    code = currency.code,
                    balance = balance,
                    usdValue = usdValue,
                    imageUrl = currency.colorfulImageUrl
                )
            }
        }


    }

}

@Composable
private fun ConstraintLayoutScope.OperatorsCard(
    sendId: ConstrainedLayoutReference,
    totalBalanceId: ConstrainedLayoutReference,
    receiveId: ConstrainedLayoutReference
) {
    Column(modifier = Modifier.constrainAs(ref = sendId) {
        start.linkTo(parent.start, margin = 80.dp)
        top.linkTo(totalBalanceId.bottom, 32.dp)
    }, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(id = R.drawable.wallet_ic_send), contentDescription = "Send")
        Text(text = "Send", fontSize = 16.sp, color = CardTextWhiteColor)
    }

    Column(modifier = Modifier.constrainAs(ref = receiveId) {
        end.linkTo(parent.end, margin = 80.dp)
        top.linkTo(totalBalanceId.bottom, 32.dp)
    },horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(id = R.drawable.wallet_ic_receive), contentDescription = "Receive")
        Text(text = "Receive", fontSize = 16.sp, color = CardTextWhiteColor)
    }
}

@Composable
private fun ConstraintLayoutScope.TotalBalanceCard(
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
            modifier = Modifier.height(32.dp).padding(horizontal = 5.dp),
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
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(text = buildAnnotatedString() {
            withStyle(SpanStyle(color = CardTextGrayColor, fontSize = 22.sp)) {
                append("$ ")
            }
            withStyle(SpanStyle(color = CardTextWhiteColor, fontSize = 26.sp, fontWeight = FontWeight.Bold)) {
                append(walletData.totalUsdValue.toString())
            }
            withStyle(SpanStyle(color = CardTextGrayColor, fontSize = 23.sp, fontWeight = FontWeight.Bold)) {
                append(" USD")
            }
        })
    }
}


//@Composable
//internal fun TotalBalanceCard(totalUsdValue: Double) {
//    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = CardBackgroundColor
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "总资产",
//                fontSize = 16.sp,
//                color = MaterialTheme.colorScheme.onPrimaryContainer
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = formatter.format(totalUsdValue),
//                fontSize = 28.sp,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.onPrimaryContainer
//            )
//        }
//    }
//}

@Composable
fun CurrencyItem(
    name: String,
    code: String,
    balance: Double,
    usdValue: Double,
    imageUrl: String
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)

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

            // 货币信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$balance $code",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 美元价值
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatter.format(usdValue),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}