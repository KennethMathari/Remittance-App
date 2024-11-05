package org.tawakal.composemphelloworld.utils

interface EncryptionManager {
    fun encryptData(data: String): ByteArray

    fun decryptData(encryptedData: ByteArray): String
}