package com.christhperalta.activosfijos.feature.login.domain.model

data class LoginResponse(
    val odataMetadata: String?,
    val sessionId: String?,
    val sessionTimeout: Int?,
    val version: String?
)
