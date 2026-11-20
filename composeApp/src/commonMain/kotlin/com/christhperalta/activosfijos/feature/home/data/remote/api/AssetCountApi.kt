package com.christhperalta.activosfijos.feature.home.data.remote.api

/**
 * Las cuatro consultas de conteo que necesita el Home, como contrato aparte de su
 * implementación Ktor ([HomeApiService]).
 *
 * Existe para que [com.christhperalta.activosfijos.feature.home.data.repository.HomeRepositoryImpl]
 * se pueda probar con un doble en memoria: el proyecto no tiene librería de mocking ni
 * `ktor-client-mock`, así que sin este contrato la única forma de testear el repositorio sería
 * levantar un cliente HTTP de verdad.
 */
interface AssetCountApi {
    suspend fun countPendingToScan(): Int
    suspend fun countGoodCondition(): Int
    suspend fun countMaintenance(): Int
    suspend fun countPendingDecommission(): Int
}
