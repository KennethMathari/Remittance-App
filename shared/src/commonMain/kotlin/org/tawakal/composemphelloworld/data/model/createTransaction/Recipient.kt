package org.tawakal.composemphelloworld.data.model.createTransaction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipient(
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("mobilePhone")
    val mobilePhone: String,
    @SerialName("recipientObjectId")
    val recipientObjectId: String,
    @SerialName("relationshipCode")
    val relationshipCode: String,
    @SerialName("usageLocation")
    val usageLocation: String
)