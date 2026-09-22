package com.christhperalta.activosfijos.core.utils

import kotlinx.cinterop.*
import platform.CommonCrypto.*
import platform.Foundation.*
import platform.Security.*

actual class CryptoUtil actual constructor() {

    private companion object {
        private const val KEYCHAIN_SERVICE = "com.b1pos.crypto"
        private const val KEYCHAIN_KEY_LABEL = "B1PosEncryptionKey"
        private const val AES_KEY_SIZE = 32
        private const val GCM_TAG_LENGTH = 128u
        private const val IV_SIZE = 12
    }

    private val secretKey: ByteArray by lazy {
        val existing = loadKeyFromKeychain()
        if (existing != null) existing else {
            val newKey = generateRandomKey()
            saveKeyToKeychain(newKey)
            newKey
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun encrypt(plaintext: String): String {
        val key = secretKey
        val iv = generateRandomIV()
        val plainBytes = plaintext.encodeToByteArray()

        val bufferSize = plainBytes.size + CC_SHA256_DIGEST_LENGTH.toInt()
        val encrypted = ByteArray(bufferSize)

        val status = memscoped {
            CCCrypt(
                op = kCCEncrypt,
                alg = kCCAlgorithmAES,
                options = kCCOptionPKCS7Padding,
                key = key.toCValues().ptr,
                keyLength = key.size.toULong(),
                iv = iv.toCValues().ptr,
                dataIn = plainBytes.toCValues().ptr,
                dataInLength = plainBytes.size.toULong(),
                dataOut = encrypted.toCValues().ptr,
                dataOutAvailable = bufferSize.toULong(),
                dataOutMoved = alloc<ULongVar>().ptr
            )
        }

        if (status != kCCSuccess) {
            throw IllegalStateException("Encryption failed with status: $status")
        }

        val combined = iv + encrypted
        return combined.encodeBase64()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun decrypt(ciphertext: String?): String {
        if (ciphertext.isNullOrEmpty()) return ""
        return try {
            val combined = ciphertext.decodeBase64()
            if (combined.size < IV_SIZE) return ""
            val iv = combined.copyOfRange(0, IV_SIZE)
            val encrypted = combined.copyOfRange(IV_SIZE, combined.size)

            val bufferSize = encrypted.size
            val decrypted = ByteArray(bufferSize)

            val status = memscoped {
                CCCrypt(
                    op = kCCDecrypt,
                    alg = kCCAlgorithmAES,
                    options = kCCOptionPKCS7Padding,
                    key = secretKey.toCValues().ptr,
                    keyLength = secretKey.size.toULong(),
                    iv = iv.toCValues().ptr,
                    dataIn = encrypted.toCValues().ptr,
                    dataInLength = encrypted.size.toULong(),
                    dataOut = decrypted.toCValues().ptr,
                    dataOutAvailable = bufferSize.toULong(),
                    dataOutMoved = alloc<ULongVar>().ptr
                )
            }

            if (status != kCCSuccess) {
                return ""
            }

            decrypted.decodeToString()
        } catch (e: Exception) {
            ""
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun generateRandomKey(): ByteArray {
        val key = ByteArray(AES_KEY_SIZE)
        SecRandomCopyBytes(kSecRandomDefault, AES_KEY_SIZE.toULong(), key.toCValues().ptr)
        return key
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun generateRandomIV(): ByteArray {
        val iv = ByteArray(IV_SIZE)
        SecRandomCopyBytes(kSecRandomDefault, IV_SIZE.toULong(), iv.toCValues().ptr)
        return iv
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun saveKeyToKeychain(key: ByteArray) {
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassGenericPassword,
            kSecAttrService to KEYCHAIN_SERVICE,
            kSecAttrAccount to KEYCHAIN_KEY_LABEL,
            kSecValueData to key.toNSData(),
            kSecAttrAccessible to kSecAttrAccessibleWhenUnlockedThisDeviceOnly
        )
        SecItemAdd(query as CFDictionaryRef, null)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun loadKeyFromKeychain(): ByteArray? {
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassGenericPassword,
            kSecAttrService to KEYCHAIN_SERVICE,
            kSecAttrAccount to KEYCHAIN_KEY_LABEL,
            kSecReturnData to true,
            kSecMatchLimit to kSecMatchLimitOne
        )
        memscoped {
            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query as CFDictionaryRef, result.ptr)
            if (status == errSecSuccess) {
                val data = result.value
                if (data != null) {
                    return data.toByteArray()
                }
            }
        }
        return null
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.encodeBase64(): String {
    val data = this.toNSData()
    return data.base64EncodedStringWithOptions(NSDataBase64EncodingOptions(0u))
}

@OptIn(ExperimentalForeignApi::class)
private fun String.decodeBase64(): ByteArray {
    val data = NSData.createWithBase64EncodedString(this, NSDataBase64DecodingOptions(0u))
        ?: throw IllegalArgumentException("Invalid Base64 input")
    return data.toByteArray()
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData {
    return memscoped {
        NSData.createWithBytes(this.toCValues().ptr, this.size.toULong())
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = this.length.toInt()
    val bytes = ByteArray(size)
    if (size > 0) {
        bytes.usePinned { pinned ->
            kotlinx.cinterop.memcpy(pinned.addressOf(0), this.bytes, this.length)
        }
    }
    return bytes
}
