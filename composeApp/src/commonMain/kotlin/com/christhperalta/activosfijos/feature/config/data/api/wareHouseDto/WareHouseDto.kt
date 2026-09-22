package com.christhperalta.activosfijos.feature.config.data.api.wareHouseDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WareHouseDto(
    @SerialName("odata.metadata")
    val odataMetadata: String? = null,
    @SerialName("value")
    val value: List<WareHouseValue> = emptyList()
)