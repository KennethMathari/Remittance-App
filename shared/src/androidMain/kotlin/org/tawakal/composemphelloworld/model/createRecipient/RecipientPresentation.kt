package org.tawakal.composemphelloworld.model.createRecipient

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RecipientPresentation(
    val country: String,
    val displayName: String,
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val phone: String,
    val usageLocation: String
): Parcelable
