package com.christhperalta.activosfijos.core.quotation

data class Product(
    val itemCode: String? = null,
    val itemDescription: String? = null,
    val branch: String? = null,
    val department: String? = null,
    val office: String? = null,
    val assetStatus: String? = null,
    val quantity : Int? = 1,
    val whsCode: String? = null,
    val baseLine : String? = "0",
    val taxCode : String? = "ITBIS",
    val unitPrice : Long? = null
)
