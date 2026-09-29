package com.christhperalta.activosfijos.feature.inventory.domain

import kotlinx.coroutines.flow.StateFlow

interface InventoryRepository {
    val summary: StateFlow<InventorySummary>
    val scannedAssets: StateFlow<List<Asset>>

    fun findAsset(code: String): Asset?
    fun recordScan(code: String)
    fun registrationFor(code: String): AssetRegistration?
    fun register(code: String, registration: AssetRegistration): Boolean
    fun pendingAssets(): List<Asset>
    fun markNotFound(code: String): Boolean
}
