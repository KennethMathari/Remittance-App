package org.tawakal.composemphelloworld.data.model.listRecipient


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("Recipients")
    val recipients: List<Recipient>
)