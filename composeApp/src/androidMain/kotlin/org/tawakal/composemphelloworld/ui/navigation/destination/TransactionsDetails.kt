package org.tawakal.composemphelloworld.ui.navigation.destination

import kotlinx.serialization.Serializable
import org.tawakal.composemphelloworld.model.listrecipients.RecipientPresentation


@Serializable
data class TransactionsDetails(
    val recipientDomain: RecipientPresentation
)
