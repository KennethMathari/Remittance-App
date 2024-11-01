package org.tawakal.composemphelloworld.data.model.listRecipient


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
    @SerialName("objectId")
    val objectId: String,
    @SerialName("transactions")
    val transactions: List<Transaction>,
    @SerialName("usageLocation")
    val usageLocation: String
)