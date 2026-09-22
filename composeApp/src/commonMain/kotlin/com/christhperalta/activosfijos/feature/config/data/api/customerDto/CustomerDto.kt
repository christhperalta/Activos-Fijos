package com.christhperalta.activosfijos.feature.config.data.api.customerDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerDto(
    @SerialName("odata.metadata")
    val odataMetadata: String? = null,
    @SerialName("value")
    val value: List<CustomerValue> = emptyList()
)