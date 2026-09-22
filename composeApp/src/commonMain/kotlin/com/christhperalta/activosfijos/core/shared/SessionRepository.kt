package com.christhperalta.activosfijos.core.shared

// commonMain
class SessionRepository {
    private var sessionId: String? = null

    fun saveSession(id: String) {
        sessionId = id
    }

    fun getSession(): String {
        return sessionId ?: throw IllegalStateException("No hay sesión activa")
    }

    fun clearSession() {
        sessionId = null
    }
}