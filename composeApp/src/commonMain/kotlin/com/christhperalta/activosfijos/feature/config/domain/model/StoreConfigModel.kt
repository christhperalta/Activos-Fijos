package com.christhperalta.activosfijos.feature.config.domain.model

data class StoreConfigModel(
    val sellerCode: Int? = null,
    val sellerName: String? = null,
    val warehouseCode: String? = null,
    val warehouseName: String? = null,
    val customerCode: String? = null,
    val customerName: String? = null,
    val priceCode: String? = null,
    val priceName: String? = null,
    val terminalNumber: String? = null,
    val cardCode: String? = null,
    val cardName: String? = null,
    val docCurrency: String? = null,
    val taxCode: String? = null,
    val rnc : String? = null
)