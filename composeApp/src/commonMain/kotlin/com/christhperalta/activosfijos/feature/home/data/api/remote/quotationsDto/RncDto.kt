package com.christhperalta.activosfijos.feature.home.data.api.remote.quotationsDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RncDto(
    @SerialName("Estatus")
    val estatus: String? = null,
    @SerialName("Name")
    val name: String? = null,
    @SerialName("Rnc")
    val rnc: String? = null
)