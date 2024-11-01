package org.tawakal.composemphelloworld

data class UserAccount(
    val username: String,
    val accountId: String,
    val mobile: String,
    val city: String,
    val name: String,
    val givenName: String,
    val familyName: String,
    val streetAddress: String,
    val referralVal: Boolean,
    var approve: Boolean
)