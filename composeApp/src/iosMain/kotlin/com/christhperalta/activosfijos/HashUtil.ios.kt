package com.christhperalta.activosfijos.core.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toCValues
import platform.CommonCrypto.CC_SHA256
import platform.CommonCrypto.CC_SHA256_DIGEST_LENGTH

@OptIn(ExperimentalForeignApi::class)
actual fun sha256(input: String): String {
    val data = input.encodeToByteArray()
    val digest = ByteArray(CC_SHA256_DIGEST_LENGTH.toInt())
    CC_SHA256(data.toCValues().ptr, data.size.toULong(), digest.toCValues().ptr)
    return digest.joinToString("") { "%02x".format(it.toUByte().toInt()) }
}
