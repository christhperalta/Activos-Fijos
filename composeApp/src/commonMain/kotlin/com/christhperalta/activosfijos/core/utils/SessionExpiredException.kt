package com.christhperalta.activosfijos.core.utils

/**
 * Se lanza cuando el `B1SESSION` guardado ya no sirve: el Service Layer responde 401 o no hay
 * ninguna sesión en memoria. Es un caso distinto de [SapException] porque la UI no lo resuelve
 * reintentando, sino devolviendo al usuario al login.
 */
class SessionExpiredException(
    errorMessage: String? = null,
) : Exception(errorMessage ?: "La sesión ha expirado")
