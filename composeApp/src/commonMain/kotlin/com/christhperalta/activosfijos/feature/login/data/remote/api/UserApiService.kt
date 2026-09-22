package com.christhperalta.activosfijos.feature.login.data.remote.api
import com.christhperalta.activosfijos.core.shared.SessionRepository
import com.christhperalta.activosfijos.core.shared.UserNameRepository
import com.christhperalta.activosfijos.core.utils.SapErrorResponse
import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.config.domain.repository.ServerConfigRepository
import com.christhperalta.activosfijos.feature.login.data.remote.dto.loginDto.LoginDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class PosApiService(
    private val client: HttpClient,
    private val serverConfigRepository: ServerConfigRepository,
    private val sessionRepository: SessionRepository,
    private val userNameRepository: UserNameRepository
) {
    private suspend fun getBaseUrl(): String {
        val config = serverConfigRepository.getServerConfig()
            ?: throw IllegalStateException("No hay configuración de servidor guardada")
        return "${config.protocol}://${config.host}:${config.port}"
    }

    private suspend fun getDatabaseName(): String {
        val config = serverConfigRepository.getServerConfig()
            ?: throw IllegalStateException("No hay configuración de servidor guardada")
        return config.databaseName
    }

    suspend fun login(userName: String, password: String): LoginDto {
        val baseUrl = getBaseUrl()
        val companyDB = getDatabaseName()
        val response = client.post("$baseUrl/b1s/v1/Login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(companyDB = companyDB, userName = userName, password = password))
        }
        if (!response.status.isSuccess()) {
            val errorBody = response.body<SapErrorResponse>()
            throw SapException(
                code = errorBody.error?.code,
                errorMessage = errorBody.error?.message?.value
            )
        }
        sessionRepository.saveSession(response.body<LoginDto>().sessionId)
        userNameRepository.saveName(userName)
        return response.body<LoginDto>()
    }
}

@Serializable
data class LoginRequest(
    @SerialName("CompanyDB") val companyDB: String,
    @SerialName("Password") val password: String,
    @SerialName("UserName") val userName: String,
)