package com.crypto.interview.core.model.wallet

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize // 替换@Serializable
@Keep
data class Currency(
    @SerializedName("blockchain_symbol")
    val blockchainSymbol: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("coin_id")
    val coinId: String = "",
    @SerializedName("colorful_image_url")
    val colorfulImageUrl: String = "",
    @SerializedName("contract_address")
    val contractAddress: String = "",
    @SerializedName("deposit_address_tag_name")
    val depositAddressTagName: String = "",
    @SerializedName("deposit_address_tag_type")
    val depositAddressTagType: String = "",
    @SerializedName("display_decimal")
    val displayDecimal: Int = 0,
    @SerializedName("explorer")
    val explorer: String = "",
    @SerializedName("gas_limit")
    val gasLimit: Int = 0,
    @SerializedName("gray_image_url")
    val grayImageUrl: String = "",
    @SerializedName("has_deposit_address_tag")
    val hasDepositAddressTag: Boolean = false,
    @SerializedName("is_erc20")
    val isErc20: Boolean = false,
    @SerializedName("min_balance")
    val minBalance: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("num_confirmation_required")
    val numConfirmationRequired: Int = 0,
    @SerializedName("supports_legacy_address")
    val supportsLegacyAddress: Boolean = false,
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("token_decimal")
    val tokenDecimal: Int = 0,
    @SerializedName("token_decimal_value")
    val tokenDecimalValue: String = "",
    @SerializedName("trading_symbol")
    val tradingSymbol: String = "",
    @SerializedName("withdrawal_eta")
    val withdrawalEta: List<String> = listOf()
) : Parcelable // 新增接口实现

