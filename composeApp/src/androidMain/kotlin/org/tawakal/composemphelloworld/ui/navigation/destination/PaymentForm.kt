package org.tawakal.composemphelloworld.ui.navigation.destination

import kotlinx.serialization.Serializable
import org.tawakal.composemphelloworld.model.createRecipient.RecipientPresentation

@Serializable
data class PaymentForm(
    val recipient: RecipientPresentation
)
