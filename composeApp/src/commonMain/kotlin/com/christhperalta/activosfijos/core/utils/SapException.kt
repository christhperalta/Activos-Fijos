package com.christhperalta.activosfijos.core.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class SapErrorMessage(
    val lang: String?,
    val value: String?
)
@Serializable
data class SapErrorDetail(
    val code: Int?,
    val message: SapErrorMessage?
)
@Serializable
data class SapErrorResponse(
    val error: SapErrorDetail?
)

// Excepción personalizada
// @Serializable
class SapException(
    val code: Int?,
    @SerialName("error")
    val errorMessage: String?
) : Exception("SAP Error [$code]: $errorMessage")