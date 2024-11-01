package org.tawakal.composemphelloworld


sealed class AuthResult {
    data class Success(val account: UserAccount?) : AuthResult()
    data class Failure(val error: String, val exception: Throwable? = null) : AuthResult()
}