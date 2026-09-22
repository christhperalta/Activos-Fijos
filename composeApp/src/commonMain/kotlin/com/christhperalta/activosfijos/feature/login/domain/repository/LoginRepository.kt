package com.christhperalta.activosfijos.feature.login.domain.repository

import com.christhperalta.activosfijos.feature.login.domain.model.LoginResponse

interface LoginRepository {
    suspend fun userLogin ( userName : String , password : String) : Result<LoginResponse>
}