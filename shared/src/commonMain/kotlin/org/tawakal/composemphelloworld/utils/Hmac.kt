package org.tawakal.composemphelloworld.utils

fun interface Hmac {

    fun hmacSHA256(key: ByteArray, data: ByteArray): ByteArray
}