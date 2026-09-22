package com.christhperalta.activosfijos.feature.login.domain.use_case

import com.christhperalta.activosfijos.feature.login.domain.model.LoginResponse
import com.christhperalta.activosfijos.feature.login.domain.repository.LoginRepository

open class LogInUserUseCase(
    private val repository: LoginRepository
) {
    open suspend operator fun invoke(
        userName: String,
        password: String
    ): Result<LoginResponse> = repository.userLogin( userName, password)

}