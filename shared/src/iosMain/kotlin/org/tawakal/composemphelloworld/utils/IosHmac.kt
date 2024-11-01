package org.tawakal.composemphelloworld.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CCHmac
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.CoreCrypto.kCCHmacAlgSHA256

@OptIn(ExperimentalForeignApi::class)
class IosHmac : Hmac {

    override fun hmacSHA256(key: ByteArray, data: ByteArray): ByteArray {
        val hash = ByteArray(CC_SHA256_DIGEST_LENGTH)
        key.usePinned { keyPinned ->
            data.usePinned { dataPinned ->
                hash.usePinned { hashPinned ->
                    CCHmac(
                        kCCHmacAlgSHA256,
                        keyPinned.addressOf(0),
                        key.size.toULong(),
                        dataPinned.addressOf(0),
                        data.size.toULong(),
                        hashPinned.addressOf(0)
                    )
                }
            }
        }
        return hash
    }
}