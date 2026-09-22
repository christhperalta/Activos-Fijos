package com.christhperalta.activosfijos.feature.config.data.api.wareHouseDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WareHouseValue(
    @SerialName("Location")
    val location: String? = null,
    @SerialName("WarehouseCode")
    val warehouseCode: String? = null,
    @SerialName("WarehouseName")
    val warehouseName: String? = null
)