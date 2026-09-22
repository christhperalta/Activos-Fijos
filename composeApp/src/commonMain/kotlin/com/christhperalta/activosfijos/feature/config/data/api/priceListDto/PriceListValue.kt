package com.christhperalta.activosfijos.feature.config.data.api.priceListDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceListValue(
    @SerialName("PriceListName")
    val priceListName: String? = null,
    @SerialName("PriceListNo")
    val priceListNo: Int? = null
)