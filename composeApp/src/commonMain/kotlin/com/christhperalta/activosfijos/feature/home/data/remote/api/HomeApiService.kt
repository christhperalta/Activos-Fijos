package com.christhperalta.activosfijos.feature.home.data.remote.api

import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.utils.SapErrorResponse
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.core.utils.SessionExpiredException
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

class HomeApiService(
    private val client: HttpClient,
    private val serverConfigRepository: ServerConfigRepository,
    private val sessionRepository: SessionRepository
) : AssetCountApi {

    private suspend fun getBaseUrl(): String {
        val config = serverConfigRepository.getServerConfig()
            ?: throw IllegalStateException("No hay configuración de servidor guardada")
        return "${config.protocol}://${config.host}:${config.port}"
    }

    override suspend fun countPendingToScan(): Int = count(FILTER_PENDING_TO_SCAN)

    override suspend fun countGoodCondition(): Int = count(FILTER_GOOD_CONDITION)

    override suspend fun countMaintenance(): Int = count(FILTER_MAINTENANCE)

    override suspend fun countPendingDecommission(): Int = count(FILTER_PENDING_DECOMMISSION)

    /**
     * `Items/$count` devuelve el entero como texto plano, no como JSON: hay que leerlo con
     * [bodyAsText] y no con `body<T>()`, que fallaría al negociar el contenido.
     */
    private suspend fun count(filter: String): Int {
        val baseUrl = getBaseUrl()
        val sessionId = currentSessionId()
        val response = client.get("$baseUrl/b1s/v1/Items/\$count") {
            header("Cookie", "B1SESSION=$sessionId")
            parameter("\$filter", filter)
        }

        // El 401 se evalúa primero: si no, la sesión expirada quedaría tapada como un error
        // genérico de SAP y la UI no podría mandar al usuario al login.
        if (response.status == HttpStatusCode.Unauthorized) {
            // El cuerpo crudo solo sirve para diagnóstico; la UI muestra su propio mensaje.
            throw SessionExpiredException(response.bodyAsText().trim().takeIf { it.isNotBlank() })
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()
            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }

        return response.bodyAsText().trim().toInt()
    }

    private fun currentSessionId(): String = try {
        sessionRepository.getSession()
    } catch (e: IllegalStateException) {
        // Mismo caso de usuario que un 401: no hay sesión con la que pedir nada.
        throw SessionExpiredException(e.message)
    }

    private companion object {
        const val FIXED_ASSETS = "ItemType eq 'itFixedAssets'"

        const val FILTER_PENDING_TO_SCAN = "$FIXED_ASSETS and U_FechaEscaneo eq null"
        const val FILTER_GOOD_CONDITION = "$FIXED_ASSETS and U_EstatusEscaneo eq 'BUEN_ESTADO'"
        const val FILTER_MAINTENANCE = "$FIXED_ASSETS and U_EstatusEscaneo eq 'MANTENIMIENTO'"
        const val FILTER_PENDING_DECOMMISSION =
            "$FIXED_ASSETS and U_EstatusEscaneo eq 'PENDIENTE_BAJA'"
    }
}
