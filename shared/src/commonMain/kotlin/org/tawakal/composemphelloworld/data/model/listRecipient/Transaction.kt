package org.tawakal.composemphelloworld.data.model.listRecipient


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    @SerialName("balance")
    val balance: Double,
    @SerialName("fee")
    val fee: Double,
    @SerialName("fulfillmentTimestamp")
    val fulfillmentTimestamp: String,
    @SerialName("paymentService")
    val paymentService: String,
    @SerialName("rate")
    val rate: Double,
    @SerialName("recipientAmount")
    val recipientAmount: Double,
    @SerialName("recipientCurrency")
    val recipientCurrency: String,
    @SerialName("senderCurrency")
    val senderCurrency: String,
    @SerialName("service")
    val service: Service,
    @SerialName("status")
    val status: String,
    @SerialName("timestamp")
    val timestamp: String,
    @SerialName("txnId")
    val txnId: String
)