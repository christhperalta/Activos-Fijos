package com.christhperalta.activosfijos.feature.inventory.data

import com.christhperalta.activosfijos.feature.inventory.domain.AssetCondition
import com.christhperalta.activosfijos.feature.inventory.domain.AssetRegistration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FakeInventoryRepositoryTest {

    private val repository = FakeInventoryRepository()

    @Test
    fun recordScan_addsAssetToHistory() {
        repository.recordScan("AF-000125")
        assertEquals(listOf("AF-000125"), repository.scannedAssets.value.map { it.code })
    }

    @Test
    fun recordScan_ignoresUnknownCodes() {
        repository.recordScan("UNKNOWN-001")
        assertTrue(repository.scannedAssets.value.isEmpty())
    }

    @Test
    fun recordScan_deduplicatesByCode() {
        repository.recordScan("AF-000125")
        repository.recordScan("af-000125")
        repository.recordScan("AF-000127")
        assertEquals(listOf("AF-000125", "AF-000127"), repository.scannedAssets.value.map { it.code })
    }

    @Test
    fun scannedAssetWithInitialRegistration_isPending() {
        repository.recordScan("AF-000125")
        assertTrue(repository.registrationFor("AF-000125") == null)
    }

    @Test
    fun scannedAssetShowsRegistrationAfterRegister() {
        repository.recordScan("AF-000125")
        val registered = repository.register("AF-000125", AssetRegistration(AssetCondition.GOOD))
        assertTrue(registered)
        assertEquals(AssetCondition.GOOD, repository.registrationFor("AF-000125")?.condition)
    }

    @Test
    fun duplicateRegistrationRejected() {
        repository.register("AF-000125", AssetRegistration(AssetCondition.GOOD))
        assertFalse(repository.register("AF-000125", AssetRegistration(AssetCondition.MAINTENANCE)))
    }

    @Test
    fun updateAssetClassification_updatesTheAsset() {
        val updated = repository.updateAssetClassification(
            code = "AF-000125",
            category = "Mobiliario",
            location = "Dirección",
            branch = "SUCURSAL SANTIAGO",
            position = "ANALISTA TECNOLOGIA 1",
        )

        assertTrue(updated)
        assertEquals("Mobiliario", repository.findAsset("AF-000125")?.category)
        assertEquals("Dirección", repository.assets.value.first { it.code == "AF-000125" }.location)
        assertEquals("SUCURSAL SANTIAGO", repository.findAsset("AF-000125")?.branch)
        assertEquals("ANALISTA TECNOLOGIA 1", repository.findAsset("AF-000125")?.position)
    }

    @Test
    fun updateAssetClassification_updatesScannedAsset() {
        repository.recordScan("AF-000125")

        val updated = repository.updateAssetClassification(
            code = "AF-000125",
            category = "Equipos de oficina",
            location = "Recursos Humanos",
            branch = "SUCURSAL PRINCIPAL - ND",
            position = "ASISTENTE ADMINISTRACION",
        )

        assertTrue(updated)
        assertEquals("Equipos de oficina", repository.scannedAssets.value.single().category)
        assertEquals("Recursos Humanos", repository.scannedAssets.value.single().location)
        assertEquals("SUCURSAL PRINCIPAL - ND", repository.scannedAssets.value.single().branch)
        assertEquals("ASISTENTE ADMINISTRACION", repository.scannedAssets.value.single().position)
    }

    @Test
    fun updateAssetClassification_rejectsUnknownAssetAndInvalidOptions() {
        assertFalse(
            repository.updateAssetClassification(
                code = "UNKNOWN-001",
                category = "Mobiliario",
                location = "Dirección",
                branch = "SUCURSAL PRINCIPAL",
                position = "ANALISTA CONTABILIDAD",
            )
        )
        assertFalse(
            repository.updateAssetClassification(
                code = "AF-000125",
                category = "Categoría inexistente",
                location = "Dirección",
                branch = "SUCURSAL PRINCIPAL",
                position = "ANALISTA CONTABILIDAD",
            )
        )
        assertEquals("Equipos tecnológicos", repository.findAsset("AF-000125")?.category)
    }
}
