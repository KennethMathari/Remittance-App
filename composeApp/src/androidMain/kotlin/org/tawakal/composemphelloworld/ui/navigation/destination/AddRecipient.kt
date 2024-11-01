package org.tawakal.composemphelloworld.ui.navigation.destination

import kotlinx.serialization.Serializable
import org.tawakal.composemphelloworld.model.validaterecipient.ValidateRecipientDataPresentation

@Serializable
data class AddRecipient(
    val validateRecipientDataPresentation: ValidateRecipientDataPresentation
)