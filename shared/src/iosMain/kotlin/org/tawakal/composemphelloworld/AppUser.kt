package org.tawakal.composemphelloworld

import platform.Foundation.*
import cocoapods.MSAL.*
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class AppUser {
    companion object {
        val shared: AppUser = AppUser()
    }
    var currentAccount: MSALAccount? = null
    var application: MSALPublicClientApplication? = null
    var webViewParameters: MSALWebviewParameters? = null
    var isLoggedIn: Boolean = false
}