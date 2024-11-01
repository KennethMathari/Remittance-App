package org.tawakal.composemphelloworld.model.validaterecipient

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ValidateRecipientDataPresentation(
    val recipient: RecipientDataPresentation
): Parcelable
