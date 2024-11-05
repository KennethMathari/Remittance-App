package org.tawakal.composemphelloworld.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AndroidEncryptionManager: EncryptionManager {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val keyAlias = "AndroidEncryptionKey"
    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    private val secretKey = keyStore.getKey(keyAlias, null) as SecretKey

    init {
        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build()
            )
            keyGenerator.generateKey()
        }
    }

    override fun encryptData(data: String): ByteArray {
        // Generate a new IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv // Get the generated IV

        // Encrypt data
        val encryptedData = cipher.doFinal(data.toByteArray())

        // Combine IV and encrypted data (IV first, then data)
        return iv + encryptedData
    }

    override fun decryptData(encryptedData: ByteArray): String {
        // Extract the IV (first 12 bytes) and the actual encrypted data
        val iv = encryptedData.copyOfRange(0, 12) // Typically 12 bytes for GCM IV
        val actualEncryptedData = encryptedData.copyOfRange(12, encryptedData.size)

        // Initialize cipher with DECRYPT_MODE, secret key, and IV
        val gcmParameterSpec = GCMParameterSpec(128, iv) // 128-bit authentication tag length
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec)

        // Decrypt and return the original data as a String
        return String(cipher.doFinal(actualEncryptedData))
    }
}