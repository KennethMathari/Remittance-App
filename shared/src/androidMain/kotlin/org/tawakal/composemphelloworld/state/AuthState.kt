package org.tawakal.composemphelloworld.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class AuthState(
    val userDetails: UserDetails? = null,
    val errorMessage: String? = null,
    val scopes: List<String>? = null
)


@Serializable
@Parcelize
data class UserDetails(
    val country: String?,
    val unit: String?,
    val email: String?,
    val phoneNumber: String?,
    val city: String?,
    val streetAddress: String?,
    val postalCode: String?,
    val name: String?
) : Parcelable