package com.example.myapplication.domain.model.createTransaction

data class PaymentDomain(
    val balance: Double,
    val fee: Double,
    val rate: Double,
    val recipientAmount: Double,
    val recipientCurrency: String,
    val senderCurrency: String
)
