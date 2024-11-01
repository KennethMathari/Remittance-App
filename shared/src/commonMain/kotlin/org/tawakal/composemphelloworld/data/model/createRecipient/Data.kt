package org.tawakal.composemphelloworld.data.model.createRecipient


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("recipient")
    val recipient: Recipient
)