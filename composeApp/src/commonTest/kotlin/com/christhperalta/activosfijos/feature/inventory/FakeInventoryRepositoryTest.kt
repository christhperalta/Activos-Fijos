package com.christhperalta.activosfijos.feature.inventory

import com.christhperalta.activosfijos.feature.inventory.data.FakeInventoryRepository
import com.christhperalta.activosfijos.feature.inventory.domain.AssetCondition
import com.christhperalta.activosfijos.feature.inventory.domain.AssetRegistration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FakeInventoryRepositoryTest {
    @Test
    fun `initial summary is internally consistent`() {
        val summary = FakeInventoryRepository().summary.value

        assertEquals(100, summary.verifiedCount)
        assertEquals(25, summary.pendingCount)
        assertEquals(0.8f, summary.progress)
    }

    @Test
    fun `registration increases the selected condition only once`() {
        val repository = FakeInventoryRepository()

        assertTrue(repository.register("AF-000125", AssetRegistration(AssetCondition.MAINTENANCE)))
        assertFalse(repository.register("AF-000125", AssetRegistration(AssetCondition.GOOD)))

        val summary = repository.summary.value
        assertEquals(101, summary.verifiedCount)
        assertEquals(11, summary.maintenanceCount)
        assertEquals(82, summary.goodCount)
    }

    @Test
    fun `pending asset can be marked as not found`() {
        val repository = FakeInventoryRepository()

        assertTrue(repository.markNotFound("AF-000128"))

        assertEquals(3, repository.summary.value.notFoundCount)
        assertEquals(101, repository.summary.value.verifiedCount)
    }
}
