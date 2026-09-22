package com.christhperalta.activosfijos.feature.home.data.api.remote.quotationsDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuotationsDto(
    @SerialName("CardCode")
    val cardCode: String? = null,
    @SerialName("CardName")
    val cardName: String? = null,
    @SerialName("Comments")
    val comments: String? = null,
    @SerialName("DocCurrency")
    val docCurrency: String? = null,
    @SerialName("DocDate")
    val docDate: String? = null,
    @SerialName("DocDueDate")
    val docDueDate: String? = null,
    @SerialName("DocObjectCode")
    val docObjectCode: String? = null,
    @SerialName("DocType")
    val docType: String? = null,
    @SerialName("DocumentLines")
    val documentLines: List<DocumentLine?>? = null,
    @SerialName("FederalTaxID")
    val federalTaxID: String? = null,
    @SerialName("SalesPersonCode")
    val salesPersonCode: String? = null,
    @SerialName("U_B1POS")
    val uB1POS: String? = null,
    @SerialName("U_B1POS_Caja")
    val uB1POSCaja: String? = null,
    @SerialName("U_B1POS_Item")
    val uB1POSItem: String? = null,
    @SerialName("U_B1POS_T")
    val uB1POST: String? = null,
    @SerialName("U_B1POS_U")
    val uB1POSU: String? = null,
    @SerialName("U_FacFecha")
    val uFacFecha: String? = null,
    @SerialName("U_FacNit")
    val uFacNit: String? = null,
    @SerialName("U_FacNom")
    val uFacNom: String? = null,
    @SerialName("U_FacSerie")
    val uFacSerie: String? = null,
    @SerialName("U_TNegocio")
    val uTNegocio: String? = null
)