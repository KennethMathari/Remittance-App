package org.tawakal.composemphelloworld.model.validaterecipient

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RecipientDataPresentation(
    val country: String,
    val displayName: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val phone: String,
    val usageLocation: String
): Parcelable
