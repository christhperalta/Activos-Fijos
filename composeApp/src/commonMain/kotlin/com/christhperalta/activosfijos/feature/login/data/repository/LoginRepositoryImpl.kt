package com.christhperalta.activosfijos.feature.login.data.repository

import com.christhperalta.activosfijos.core.utils.SapException
import com.christhperalta.activosfijos.feature.login.data.remote.api.PosApiService
import com.christhperalta.activosfijos.feature.login.data.remote.dto.loginDto.LoginDto
import com.christhperalta.activosfijos.feature.login.domain.model.LoginResponse
import com.christhperalta.activosfijos.feature.login.domain.repository.LoginRepository


class LoginRepositoryImpl(
    private val apiService: PosApiService
) : LoginRepository {

      override suspend fun userLogin(
        userName: String,
        password: String
    ): Result<LoginResponse> {
        return try {
            val response = apiService.login( userName, password)
            Result.success(response.toDomain())
        } catch (e: SapException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}


fun LoginDto.toDomain(): LoginResponse {
    return LoginResponse(
        sessionId = this.sessionId,
        sessionTimeout = this.sessionTimeout,
        version = this.version,
        odataMetadata = this.odataMetadata
    )
}






