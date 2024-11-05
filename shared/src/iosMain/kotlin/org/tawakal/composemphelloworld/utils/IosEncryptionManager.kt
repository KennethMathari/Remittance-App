package org.tawakal.composemphelloworld.utils

import platform.CoreCrypto.kCCBlockSizeAES128
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.value
import platform.CoreCrypto.CCCrypt
import platform.CoreCrypto.kCCAlgorithmAES
import platform.CoreCrypto.kCCDecrypt
import platform.CoreCrypto.kCCEncrypt
import platform.CoreCrypto.kCCOptionPKCS7Padding
import platform.CoreCrypto.kCCSuccess
import platform.posix.size_tVar

@OptIn(ExperimentalForeignApi::class)
class IosEncryptionManager: EncryptionManager {

    private val keyAlias = "IosEncryptionKey"

    private fun getKey(): ByteArray {
        // Generate or retrieve the symmetric key
        return keyAlias.encodeToByteArray() // Simplified key for demo purposes
    }

    override fun encryptData(data: String): ByteArray {
        val key = getKey()
        val encryptedData = data.encodeToByteArray()
        return aesEncrypt(encryptedData, key)
    }

    override fun decryptData(encryptedData: ByteArray): String {
        val key = getKey()
        val decryptedData = aesDecrypt(encryptedData, key)
        return decryptedData.decodeToString()
    }

    private fun aesEncrypt(data: ByteArray, key: ByteArray): ByteArray {
        // AES encryption logic using CommonCrypto
        val dataLength = data.size
        val keyLength = key.size
        val encryptedData = ByteArray(dataLength + kCCBlockSizeAES128.toInt())

        // Convert ByteArray to CPointer for CommonCrypto
        memScoped {
            val numBytesEncrypted = alloc<size_tVar>()
            val status = CCCrypt(
                kCCEncrypt,                    // Operation (Encrypt)
                kCCAlgorithmAES,               // Algorithm
                kCCOptionPKCS7Padding,         // Options (PKCS7 padding)
                key.refTo(0).getPointer(memScope), // Key
                keyLength.convert(),           // Key length
                null,                          // Initialization Vector (IV) - can be null for ECB mode
                data.refTo(0).getPointer(memScope), // Input data
                dataLength.convert(),          // Input length
                encryptedData.refTo(0).getPointer(memScope), // Output buffer
                encryptedData.size.convert(),  // Output buffer size
                numBytesEncrypted.ptr          // Number of bytes written
            )

            // Check for encryption success
            if (status != kCCSuccess) {
                throw Exception("AES encryption failed with status: $status")
            }

            // Return the actual encrypted data
            return encryptedData.copyOf(numBytesEncrypted.value.toInt())
        }
    }

    private fun aesDecrypt(data: ByteArray, key: ByteArray): ByteArray {
        // AES decryption logic using CommonCrypto
        val dataLength = data.size
        val keyLength = key.size
        val decryptedData = ByteArray(dataLength + kCCBlockSizeAES128.toInt())

        // Convert ByteArray to CPointer for CommonCrypto
        memScoped {
            val numBytesDecrypted = alloc<size_tVar>()
            val status = CCCrypt(
                kCCDecrypt,                    // Operation (Decrypt)
                kCCAlgorithmAES,               // Algorithm
                kCCOptionPKCS7Padding,         // Options (PKCS7 padding)
                key.refTo(0).getPointer(memScope), // Key
                keyLength.convert(),           // Key length
                null,                          // Initialization Vector (IV) - can be null for ECB mode
                data.refTo(0).getPointer(memScope), // Input data
                dataLength.convert(),          // Input length
                decryptedData.refTo(0).getPointer(memScope), // Output buffer
                decryptedData.size.convert(),  // Output buffer size
                numBytesDecrypted.ptr          // Number of bytes written
            )

            // Check for decryption success
            if (status != kCCSuccess) {
                throw Exception("AES decryption failed with status: $status")
            }

            // Return the actual decrypted data
            return decryptedData.copyOf(numBytesDecrypted.value.toInt())
        }
    }
}