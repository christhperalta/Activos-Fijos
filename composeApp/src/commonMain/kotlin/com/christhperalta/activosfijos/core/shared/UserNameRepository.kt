package com.christhperalta.activosfijos.core.shared

class UserNameRepository  {

    private var userName: String? = null

    fun saveName(name: String) {
        userName = name
    }

    fun getName(): String {
        return userName ?: throw IllegalStateException("No hay user guardado")
    }

    fun clearName() {
        userName = null
    }
}