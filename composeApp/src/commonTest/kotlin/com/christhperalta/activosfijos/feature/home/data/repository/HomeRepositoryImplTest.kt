package com.christhperalta.activosfijos.feature.home.data.repository

import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.core.utils.SessionExpiredException
import com.christhperalta.activosfijos.feature.home.data.remote.api.AssetCountApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeRepositoryImplTest {

    @Test
    fun totalAssets_is_the_sum_of_the_four_counts() = runTest {
        val repository = HomeRepositoryImpl(
            FakeAssetCountApi(pendingToScan = 10, good = 4, maintenance = 3, pendingDecommission = 2)
        )

        val summary = repository.getAssetCountSummary().getOrThrow()

        assertEquals(19, summary.totalAssets)
        assertEquals(10, summary.pendingToScanCount)
        assertEquals(4, summary.goodCount)
        assertEquals(3, summary.maintenanceCount)
        assertEquals(2, summary.pendingDecommissionCount)
    }

    @Test
    fun verifiedCount_excludes_the_assets_not_scanned_yet() = runTest {
        val repository = HomeRepositoryImpl(
            FakeAssetCountApi(pendingToScan = 10, good = 4, maintenance = 3, pendingDecommission = 2)
        )

        val summary = repository.getAssetCountSummary().getOrThrow()

        assertEquals(9, summary.verifiedCount)
    }

    @Test
    fun progress_is_verified_over_total() = runTest {
        val repository = HomeRepositoryImpl(
            FakeAssetCountApi(pendingToScan = 1, good = 1, maintenance = 1, pendingDecommission = 1)
        )

        val summary = repository.getAssetCountSummary().getOrThrow()

        assertEquals(0.75f, summary.progress)
    }

    @Test
    fun progress_is_zero_when_there_are_no_assets() = runTest {
        val repository = HomeRepositoryImpl(
            FakeAssetCountApi(pendingToScan = 0, good = 0, maintenance = 0, pendingDecommission = 0)
        )

        val summary = repository.getAssetCountSummary().getOrThrow()

        assertEquals(0, summary.totalAssets)
        assertEquals(0f, summary.progress)
    }

    @Test
    fun SapException_becomes_a_failed_result() = runTest {
        val sapError = SapException(code = -1, errorMessage = "Invalid query option")
        val repository = HomeRepositoryImpl(FailingAssetCountApi(sapError))

        val result = repository.getAssetCountSummary()

        assertTrue(result.isFailure)
        assertEquals(sapError, result.exceptionOrNull())
    }

    @Test
    fun SessionExpiredException_becomes_a_failed_result_and_keeps_its_type() = runTest {
        val repository = HomeRepositoryImpl(FailingAssetCountApi(SessionExpiredException()))

        val result = repository.getAssetCountSummary()

        assertTrue(result.isFailure)
        // Importante que no se pierda el tipo: es lo que el ViewModel usa para decidir si
        // muestra el modal de sesión expirada en lugar del error con reintento.
        assertTrue(result.exceptionOrNull() is SessionExpiredException)
    }

    @Test
    fun generic_exception_becomes_a_failed_result() = runTest {
        val repository = HomeRepositoryImpl(FailingAssetCountApi(Exception("Network timeout")))

        val result = repository.getAssetCountSummary()

        assertTrue(result.isFailure)
        assertEquals("Network timeout", result.exceptionOrNull()?.message)
    }

    @Test
    fun the_four_endpoints_are_queried_once_each() = runTest {
        val api = FakeAssetCountApi(pendingToScan = 1, good = 1, maintenance = 1, pendingDecommission = 1)
        val repository = HomeRepositoryImpl(api)

        repository.getAssetCountSummary()

        assertEquals(1, api.pendingToScanCalls)
        assertEquals(1, api.goodCalls)
        assertEquals(1, api.maintenanceCalls)
        assertEquals(1, api.pendingDecommissionCalls)
    }
}

private class FakeAssetCountApi(
    private val pendingToScan: Int,
    private val good: Int,
    private val maintenance: Int,
    private val pendingDecommission: Int,
) : AssetCountApi {
    var pendingToScanCalls = 0
    var goodCalls = 0
    var maintenanceCalls = 0
    var pendingDecommissionCalls = 0

    override suspend fun countPendingToScan(): Int {
        pendingToScanCalls++
        return pendingToScan
    }

    override suspend fun countGoodCondition(): Int {
        goodCalls++
        return good
    }

    override suspend fun countMaintenance(): Int {
        maintenanceCalls++
        return maintenance
    }

    override suspend fun countPendingDecommission(): Int {
        pendingDecommissionCalls++
        return pendingDecommission
    }
}

private class FailingAssetCountApi(private val error: Throwable) : AssetCountApi {
    override suspend fun countPendingToScan(): Int = throw error
    override suspend fun countGoodCondition(): Int = throw error
    override suspend fun countMaintenance(): Int = throw error
    override suspend fun countPendingDecommission(): Int = throw error
}
