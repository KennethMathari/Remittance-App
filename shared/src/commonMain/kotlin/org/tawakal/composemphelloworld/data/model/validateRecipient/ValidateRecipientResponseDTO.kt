package org.tawakal.composemphelloworld.data.model.validateRecipient


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ValidateRecipientResponseDTO(
    @SerialName("data") val `data`: ValidateRecipientDataDTO,
    @SerialName("source") val source: String,
    @SerialName("status") val status: String,
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("timeStamp") val timeStamp: String
)
