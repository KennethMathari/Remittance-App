package org.tawakal.composemphelloworld.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class AndroidHmac : Hmac {
    override fun hmacSHA256(key: ByteArray, data: ByteArray): ByteArray {
        val hmac = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(key, "HmacSHA256")
        hmac.init(secretKeySpec)
        return hmac.doFinal(data)
    }
}