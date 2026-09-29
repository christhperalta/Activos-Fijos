package com.christhperalta.activosfijos.feature.inventory.data

import com.christhperalta.activosfijos.feature.inventory.domain.Asset
import com.christhperalta.activosfijos.feature.inventory.domain.AssetCondition
import com.christhperalta.activosfijos.feature.inventory.domain.AssetRegistration
import com.christhperalta.activosfijos.feature.inventory.domain.InventoryRepository
import com.christhperalta.activosfijos.feature.inventory.domain.InventorySummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** In-memory source used until the inventory API and offline store are available. */
class FakeInventoryRepository : InventoryRepository {
    private val assets = listOf(
        Asset(
            code = "AF-000125",
            name = "Laptop Dell Latitude",
            serialNumber = "SN-8H73K92",
            category = "Equipos tecnologicos",
            location = "Contabilidad",
            responsible = "Juan Perez",
        ),
        Asset(
            code = "AF-000126",
            name = "Monitor Dell 24 pulgadas",
            serialNumber = "SN-2K91D43",
            category = "Equipos tecnologicos",
            location = "Contabilidad",
            responsible = "Maria Rodriguez",
        ),
        Asset(
            code = "AF-000127",
            name = "Impresora HP LaserJet",
            serialNumber = "SN-9P12H77",
            category = "Equipos de oficina",
            location = "Recursos Humanos",
            responsible = "Ana Martinez",
        ),
        Asset(
            code = "AF-000128",
            name = "Silla ergonomica",
            serialNumber = "SIN SERIE",
            category = "Mobiliario",
            location = "Direccion",
            responsible = "Carlos Gomez",
        ),
    )

    private val registrations = mutableMapOf<String, AssetRegistration>()
    private val _summary = MutableStateFlow(
        InventorySummary(
            totalAssets = 125,
            goodCount = 82,
            maintenanceCount = 10,
            pendingDecommissionCount = 6,
            notFoundCount = 2,
        )
    )
    private val _scannedAssets = MutableStateFlow<List<Asset>>(emptyList())

    override val summary: StateFlow<InventorySummary> = _summary.asStateFlow()
    override val scannedAssets: StateFlow<List<Asset>> = _scannedAssets.asStateFlow()

    override fun findAsset(code: String): Asset? =
        assets.firstOrNull { it.code.equals(code.trim(), ignoreCase = true) }

    override fun recordScan(code: String) {
        val asset = findAsset(code) ?: return
        _scannedAssets.update { history ->
            if (history.any { it.code == asset.code }) history else history + asset
        }
    }

    override fun registrationFor(code: String): AssetRegistration? = registrations[code]

    override fun register(code: String, registration: AssetRegistration): Boolean {
        val asset = findAsset(code) ?: return false
        if (registrations.containsKey(asset.code)) return false
        registrations[asset.code] = registration
        updateSummary(registration.condition)
        return true
    }

    override fun pendingAssets(): List<Asset> =
        assets.filterNot { registrations.containsKey(it.code) }

    override fun markNotFound(code: String): Boolean =
        register(code, AssetRegistration(condition = AssetCondition.NOT_FOUND))

    private fun updateSummary(condition: AssetCondition) {
        _summary.update { summary ->
            when (condition) {
                AssetCondition.GOOD -> summary.copy(goodCount = summary.goodCount + 1)
                AssetCondition.MAINTENANCE -> summary.copy(maintenanceCount = summary.maintenanceCount + 1)
                AssetCondition.PENDING_DECOMMISSION -> summary.copy(
                    pendingDecommissionCount = summary.pendingDecommissionCount + 1
                )
                AssetCondition.NOT_FOUND -> summary.copy(notFoundCount = summary.notFoundCount + 1)
            }
        }
    }
}
