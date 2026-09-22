package com.christhperalta.activosfijos.feature.config.presentation

import com.christhperalta.activosfijos.feature.config.domain.model.Customer
import com.christhperalta.activosfijos.feature.config.domain.model.PriceList
import com.christhperalta.activosfijos.feature.config.domain.model.Seller
import com.christhperalta.activosfijos.feature.config.domain.model.Warehouse


data class ConfigUiState(
    // Manager Credentials
    val username: String? = null,
    val password: String? = null,
    val isManagerSaved: Boolean = false,
    // Server
    val protocol: String? = null,
    val host: String? = null,
    val port: String? = null,
    val databaseName: String? = null,
    val isServerSaved: Boolean = false,
    val isTesting: Boolean = false,
    val testResult: TestResult? = null,
    // Store
    val sellerCode: Int? = null,
    val sellerName: String? = null,
    val warehouseCode: String? = null,
    val warehouseName: String? = null,
    val customerCode: String? = null,
    val customerName: String? = null,
    val priceListCode: String? = null,
    val priceListName: String? = null,
    val terminalNumber: String? = null,
    val cardCode: String? = null,
    val cardName: String? = null,
    val docCurrency: String? = null,
    val taxCode: String? = null,
    val isStoreSaved: Boolean = false,
    val rnc : String? = null,
    // General
    val isLoading: Boolean = false,
    val error: String? = null,
    // Lists
    val sellerList: List<Seller> = emptyList(),
    val warehouseList: List<Warehouse> = emptyList(),
    val customerList: List<Customer> = emptyList(),
    val priceList: List<PriceList> = emptyList(),
)