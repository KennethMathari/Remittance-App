package org.tawakal.composemphelloworld.msal

fun interface AuthenticationManager {
    fun acquireTokenSilently()
}