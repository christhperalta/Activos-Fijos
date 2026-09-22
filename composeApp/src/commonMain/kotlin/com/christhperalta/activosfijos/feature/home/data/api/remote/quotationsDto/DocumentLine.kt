package com.christhperalta.activosfijos.feature.home.data.api.remote.quotationsDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentLine(
    @SerialName("BaseLine")
    val baseLine: String? = null,
    @SerialName("ItemCode")
    val itemCode: String? = null,
    @SerialName("ItemDescription")
    val itemDescription: String? = null,
    @SerialName("Quantity")
    val quantity: String? = null,
    @SerialName("TaxCode")
    val taxCode: String? = null,
    @SerialName("UnitPrice")
    val unitPrice: String? = null,
    @SerialName("WarehouseCode")
    val warehouseCode: String? = null
)