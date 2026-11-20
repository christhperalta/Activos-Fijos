package com.christhperalta.activosfijos.feature.home.domain.model

/**
 * Conteo de activos fijos por estado, un campo por endpoint `$count` del Service Layer.
 *
 * Todo lo demás se deriva: no hay un total que mantener sincronizado a mano, y no hay campo
 * para "no encontrados" porque no existe endpoint que lo alimente.
 */
data class AssetCountSummary(
    val pendingToScanCount: Int,
    val goodCount: Int,
    val maintenanceCount: Int,
    val pendingDecommissionCount: Int,
) {
    val verifiedCount: Int
        get() = goodCount + maintenanceCount + pendingDecommissionCount

    val totalAssets: Int
        get() = pendingToScanCount + verifiedCount

    val progress: Float
        get() = if (totalAssets == 0) 0f else verifiedCount.toFloat() / totalAssets
}
