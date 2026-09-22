package com.christhperalta.activosfijos.feature.config.data.repository

import com.christhperalta.activosfijos.feature.config.data.local.ConfigDataSource
import com.christhperalta.activosfijos.feature.config.domain.model.ServerConfigModel
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import io.ktor.client.*
import io.ktor.client.request.*


class ServerConfigRepositoryImpl(
    private val dataSource: ConfigDataSource,
    private val httpClient: HttpClient
) : ServerConfigRepository {

    override suspend fun getServerConfig(): ServerConfigModel? =
        dataSource.getServerConfig()

    override suspend fun saveServerConfig(config: ServerConfigModel) =
        dataSource.saveServerConfig(config)

    override suspend fun testConnection(config: ServerConfigModel): Result<Unit> {
        return runCatching {
            val url = "${config.protocol}://${config.host}:${config.port}/b1s/v1"
            httpClient.get(url)
        }
    }
}