package com.christhperalta.activosfijos.feature.config.data.api.sellerDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SellerDto(
    @SerialName("odata.metadata")
    val odataMetadata: String? = null,
    @SerialName("value")
    val value: List<Value> = emptyList()
)