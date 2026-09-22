package com.christhperalta.activosfijos.core.utils

import java.security.MessageDigest

actual fun sha256(input: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(input.toByteArray())
        .joinToString("") { "%02x".format(it) }
}
