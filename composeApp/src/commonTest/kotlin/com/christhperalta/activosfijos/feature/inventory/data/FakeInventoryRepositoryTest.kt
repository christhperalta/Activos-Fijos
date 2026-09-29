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
}