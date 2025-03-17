package com.crypto.interview.feature.wallet.ui

/**
 * Copyright:InterviewTest
 * Author: liyang <br>
 * Date:2025/3/16 00:01<br>
 * Desc: <br>
 */
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crypto.interview.core.domain.model.WalletData
import com.crypto.interview.core.ui.CardBackgroundColor
import com.crypto.interview.feature.wallet.ui.components.CurrencyItemCard
import com.crypto.interview.feature.wallet.ui.components.OperatorsCard
import com.crypto.interview.feature.wallet.ui.components.TopToolsBar
import com.crypto.interview.feature.wallet.ui.components.TotalBalanceCard
import com.crypto.interview.feature.wallet.ui.theme.WalletCardBackgroundColor
import com.crypto.interview.feature.wallet.viewmodel.WalletUiState
import com.crypto.interview.feature.wallet.viewmodel.WalletViewModel

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

@Composable
internal fun WalletContent(
    walletData: WalletData
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalletCardBackgroundColor)
    ) {
        val (topSpacer, settingId, scanId, brandDescId, totalBalanceId, sendId, receiveId, horizontalDividerId, currencyListId) = createRefs()
        TopToolsBar(topSpacer, settingId, scanId)
        TotalBalanceCard(brandDescId, settingId, totalBalanceId, walletData)
        OperatorsCard(sendId, totalBalanceId, receiveId, horizontalDividerId, brandDescId)
        LazyColumn(
            modifier = Modifier
                .background(
                    color = CardBackgroundColor,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
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
                val balance = walletData.walletBalances[index].amount
                val usdValue = walletData.walletBalances[index].usdValue
                CurrencyItemCard(
                    name = currency.name,
                    code = currency.code,
                    amount = balance,
                    usdValue = usdValue,
                    imageUrl = currency.colorfulImageUrl
                )
            }
        }


    }

}








