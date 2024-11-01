package org.tawakal.composemphelloworld.data.model.createTransaction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    @SerialName("balance")
    val balance: Double,
    @SerialName("fee")
    val fee: Double,
    @SerialName("rate")
    val rate: Double,
    @SerialName("recipientAmount")
    val recipientAmount: Double,
    @SerialName("recipientCurrency")
    val recipientCurrency: String,
    @SerialName("senderCurrency")
    val senderCurrency: String
)