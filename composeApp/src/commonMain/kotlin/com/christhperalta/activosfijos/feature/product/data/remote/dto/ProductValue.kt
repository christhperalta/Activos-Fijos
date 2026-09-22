package com.christhperalta.activosfijos.feature.product.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductValue(
    @SerialName("AvgPrice")
    val avgPrice: Double? = null,
    @SerialName("CodeBars")
    val codeBars: String? = null,
    @SerialName("Existencia")
    val existencia: Double? = null,
    @SerialName("id__")
    val id: Int? = null,
    @SerialName("ItemCode")
    val itemCode: String? = null,
    @SerialName("ItemName")
    val itemName: String? = null,
    @SerialName("ItmsGrpCod")
    val itmsGrpCod: Int? = null,
    @SerialName("MinLevel")
    val minLevel: Double? = null,
    @SerialName("NumInSale")
    val numInSale: Double? = null,
    @SerialName("Price")
    val price: Double? = null,
    @SerialName("PriceList")
    val priceList: Int? = null,
    @SerialName("QryGroup1")
    val qryGroup1: String? = null,
    @SerialName("QryGroup2")
    val qryGroup2: String? = null,
    @SerialName("QryGroup3")
    val qryGroup3: String? = null,
    @SerialName("QryGroup4")
    val qryGroup4: String? = null,
    @SerialName("SalUnitMsr")
    val salUnitMsr: String? = null,
    @SerialName("SuppCatNum")
    val suppCatNum: String? = null,
    @SerialName("U_ITBIS")
    val uITBIS: String? = null,
    @SerialName("U_LINEANEGOCIO")
    val uLINEANEGOCIO: String? = null,
    @SerialName("U_UBICACION")
    val uUBICACION: String? = null,
    @SerialName("VATLiable")
    val vATLiable: String? = null,
    @SerialName("WhsCode")
    val whsCode: String? = null,
)