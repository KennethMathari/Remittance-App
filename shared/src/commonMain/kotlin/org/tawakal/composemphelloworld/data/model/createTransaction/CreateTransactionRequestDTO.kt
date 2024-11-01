package com.example.myapplication.data.model.createTransaction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tawakal.composemphelloworld.data.model.createTransaction.Payment
import org.tawakal.composemphelloworld.data.model.createTransaction.Recipient
import org.tawakal.composemphelloworld.data.model.createTransaction.Service

@Serializable
data class CreateTransactionRequestDTO(
    @SerialName("payment")
    val payment: Payment,
    @SerialName("paymentMethod")
    val paymentMethod: String,
    @SerialName("purpose")
    val purpose: String,
    @SerialName("recipient")
    val recipient: Recipient,
    @SerialName("senderObjectId")
    val senderObjectId: String,
    @SerialName("service")
    val service: Service,
    @SerialName("sourceOfIncomeCode")
    val sourceOfIncomeCode: String,
    @SerialName("timestamp")
    val timestamp: String
)