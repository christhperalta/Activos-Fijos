package com.christhperalta.activosfijos.feature.home.domain.model

import com.christhperalta.activosfijos.core.quotation.Product


data class Quotations(
    val cardCode: String? = null,
    val cardName : String? = null,
    val comments: String? = null,
    val docCurrency: String? = null,
    val docDate: String? = null,
    val docDueDate: String? = null,
    val docObjectCode: String? = null,
    val docType: String? = null,
    val federalTaxID: String? = null,
    val salesPersonCode: String? = null,
    val uB1POS: String? = null,
    val uB1POSCaja: String? = null,
    val uB1POSItem: String? = null,
    val uB1POST: String? = null,
    val uB1POSU: String? = null,
    val uFacFecha: String? = null,
    val uFacNit: String? = null,
    val uFacNom: String? = null,
    val uFacSerie: String? = null,
    val uTNegocio: String? = null,
    val documentLines: List<Product?>? = null
)
