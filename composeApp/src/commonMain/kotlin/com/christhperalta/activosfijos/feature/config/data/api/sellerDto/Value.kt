package com.christhperalta.activosfijos.feature.config.data.api.sellerDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Value(
    @SerialName("id__")
    val id: Int? = null,
    @SerialName("Locked")
    val locked: String? = null,
    @SerialName("SlpCode")
    val slpCode: Int? = null,
    @SerialName("SlpName")
    val slpName: String? = null,
    @SerialName("U_tipo")
    val uTipo: String? = null
)