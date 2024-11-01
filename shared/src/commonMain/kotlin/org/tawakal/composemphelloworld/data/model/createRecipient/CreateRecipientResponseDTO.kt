package org.tawakal.composemphelloworld.data.model.createRecipient


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipientResponseDTO(
    @SerialName("body")
    val body: Body,
    @SerialName("headers")
    val headers: Headers,
    @SerialName("statusCode")
    val statusCode: String,
    @SerialName("statusCodeValue")
    val statusCodeValue: Int
)