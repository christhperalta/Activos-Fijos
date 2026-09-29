package com.christhperalta.activosfijos.feature.inventory.domain

enum class AssetCondition {
    GOOD,
    MAINTENANCE,
    PENDING_DECOMMISSION,
    NOT_FOUND,
}

data class Asset(
    val code: String,
    val name: String,
    val serialNumber: String,
    val category: String,
    val location: String,
    val responsible: String,
)

data class AssetRegistration(
    val condition: AssetCondition,
    val reason: String? = null,
    val observations: String = "",
    val hasPhoto: Boolean = false,
)

data class InventorySummary(
    val totalAssets: Int,
    val goodCount: Int,
    val maintenanceCount: Int,
    val pendingDecommissionCount: Int,
    val notFoundCount: Int,
) {
    val verifiedCount: Int
        get() = goodCount + maintenanceCount + pendingDecommissionCount + notFoundCount

    val pendingCount: Int
        get() = (totalAssets - verifiedCount).coerceAtLeast(0)

    val progress: Float
        get() = if (totalAssets == 0) 0f else verifiedCount.toFloat() / totalAssets
}
