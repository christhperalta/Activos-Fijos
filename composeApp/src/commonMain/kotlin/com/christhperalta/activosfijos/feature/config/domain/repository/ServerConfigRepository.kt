package com.christhperalta.activosfijos.feature.config.domain.repository


import com.christhperalta.activosfijos.feature.config.domain.model.ServerConfigModel

interface ServerConfigRepository {
    suspend fun getServerConfig(): ServerConfigModel?
    suspend fun saveServerConfig(config: ServerConfigModel)
    suspend fun testConnection(config: ServerConfigModel): Result<Unit>
}