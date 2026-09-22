package com.christhperalta.activosfijos.feature.product.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("odata.metadata")
    val odataMetadata: String? = null,
    @SerialName("value")
    val value: List<ProductValue> = emptyList()
)