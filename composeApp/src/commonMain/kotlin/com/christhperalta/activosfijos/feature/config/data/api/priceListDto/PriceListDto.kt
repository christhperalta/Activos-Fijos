package com.christhperalta.activosfijos.feature.config.data.api.priceListDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceListDto(
    @SerialName("odata.metadata")
    val odataMetadata: String? = null,
    @SerialName("value")
    val value: List<PriceListValue> = emptyList()
)