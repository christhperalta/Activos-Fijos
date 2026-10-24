package com.christhperalta.activosfijos.feature.inventory.domain

import kotlinx.coroutines.flow.StateFlow

interface InventoryRepository {
    val assets: StateFlow<List<Asset>>
    val summary: StateFlow<InventorySummary>
    val scannedAssets: StateFlow<List<Asset>>
    val availableCategories: List<String>
    val availableLocations: List<String>
    val availableBranches: List<String>
    val availablePositions: List<String>

    fun findAsset(code: String): Asset?
    fun updateAssetClassification(
        code: String,
        category: String,
        location: String,
        branch: String,
        position: String,
    ): Boolean
    fun recordScan(code: String)
    fun registrationFor(code: String): AssetRegistration?
    fun register(code: String, registration: AssetRegistration): Boolean
    fun pendingAssets(): List<Asset>
    fun markNotFound(code: String): Boolean
}
