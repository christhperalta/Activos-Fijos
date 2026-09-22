package com.christhperalta.activosfijos.feature.config.data.api.customerDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerValue(
    @SerialName("CardCode")
    val cardCode: String? = null,
    @SerialName("CardName")
    val cardName: String? = null,
    @SerialName("CreditLimit")
    val creditLimit: Double? = null,
    @SerialName("CurrentAccountBalance")
    val currentAccountBalance: Double? = null,
    @SerialName("FederalTaxID")
    val federalTaxID: String? = null,
    @SerialName("MailAddress")
    val mailAddress: String? = null,
    @SerialName("odata.etag")
    val odataEtag: String? = null,
    @SerialName("PayTermsGrpCode")
    val payTermsGrpCode: Int? = null,
    @SerialName("Phone1")
    val phone1: String? = null,
    @SerialName("PriceListNum")
    val priceListNum: Int? = null,
    @SerialName("U_B1POS_Membre")
    val uB1POSMembre: String? = null,
    @SerialName("U_NIT")
    val uNIT: String? = null
)