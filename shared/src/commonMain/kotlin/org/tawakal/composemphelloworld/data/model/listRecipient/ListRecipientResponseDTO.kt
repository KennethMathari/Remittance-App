package org.tawakal.composemphelloworld.data.model.listRecipient


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListRecipientResponseDTO(
    @SerialName("data")
    val `data`: Data,
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: String,
    @SerialName("statusCode")
    val statusCode: Int,
    @SerialName("timeStamp")
    val timeStamp: String
)