package org.tawakal.composemphelloworld.data.model.createRecipient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipientRequestDTO(
    @SerialName("country")
    val country: String,
    @SerialName("email")
    val email: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("relationship")
    val relationship: String,
    @SerialName("usageLocation")
    val usageLocation: String
)
