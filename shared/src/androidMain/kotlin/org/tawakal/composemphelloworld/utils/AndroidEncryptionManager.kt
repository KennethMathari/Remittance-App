package org.tawakal.composemphelloworld.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class AndroidEncryptionManager: EncryptionManager {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val keyAlias = "AndroidEncryptionKey"
    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")

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
        cipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(keyAlias, null) as SecretKey)
        return cipher.doFinal(data.toByteArray())
    }

    override fun decryptData(encryptedData: ByteArray): String {
        cipher.init(Cipher.DECRYPT_MODE, keyStore.getKey(keyAlias, null) as SecretKey)
        return String(cipher.doFinal(encryptedData))
    }
}