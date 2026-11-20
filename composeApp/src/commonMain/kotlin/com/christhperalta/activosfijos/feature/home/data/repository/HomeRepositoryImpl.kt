package com.christhperalta.activosfijos.feature.home.data.repository

import com.christhperalta.activosfijos.feature.home.data.remote.api.AssetCountApi
import com.christhperalta.activosfijos.feature.home.domain.model.AssetCountSummary
import com.christhperalta.activosfijos.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeRepositoryImpl(
    private val apiService: AssetCountApi
) : HomeRepository {

    override suspend fun getAssetCountSummary(): Result<AssetCountSummary> {
        return try {
            // Las cuatro peticiones son independientes: en paralelo el Home carga en el tiempo
            // de la más lenta, no en la suma de las cuatro.
            coroutineScope {
                val pendingToScan = async { apiService.countPendingToScan() }
                val good = async { apiService.countGoodCondition() }
                val maintenance = async { apiService.countMaintenance() }
                val pendingDecommission = async { apiService.countPendingDecommission() }

                Result.success(
                    AssetCountSummary(
                        pendingToScanCount = pendingToScan.await(),
                        goodCount = good.await(),
                        maintenanceCount = maintenance.await(),
                        pendingDecommissionCount = pendingDecommission.await(),
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
