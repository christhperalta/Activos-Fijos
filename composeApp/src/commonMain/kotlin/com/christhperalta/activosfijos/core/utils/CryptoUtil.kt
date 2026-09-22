package com.christhperalta.activosfijos.core.utils

expect class CryptoUtil() {
    fun encrypt(plaintext: String): String
    fun decrypt(ciphertext: String?): String
}
