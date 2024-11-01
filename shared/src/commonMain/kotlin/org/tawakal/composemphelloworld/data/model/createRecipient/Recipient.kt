package org.tawakal.composemphelloworld.data.model.createRecipient


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipient(
    @SerialName("country")
    val country: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("email")
    val email: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("id")
    val id: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("usageLocation")
    val usageLocation: String
)