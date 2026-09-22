package com.christhperalta.activosfijos.feature.login.presentation

import com.christhperalta.activosfijos.feature.login.domain.model.LoginResponse



data class LoginUiState(
    val isLoading: Boolean = false,
    val loginResponse: LoginResponse? = null,
    val error: String? = null,
    val odataMetadata: String? = null,
    val odataNextLink: String? = null,
    val user : String? = "",
    val password : String? = ""
)