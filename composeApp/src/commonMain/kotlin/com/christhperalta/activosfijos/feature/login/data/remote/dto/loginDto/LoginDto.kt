package com.christhperalta.activosfijos.feature.login.data.remote.dto.loginDto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    @SerialName("odata.metadata") val odataMetadata: String,
    @SerialName("SessionId") val sessionId: String,
    @SerialName("SessionTimeout") val sessionTimeout: Int,
    @SerialName("Version") val version: String
)

