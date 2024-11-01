package com.example.myapplication.domain.model.createTransaction

data class CreateTransactionRequestDomain(
    val payment: PaymentDomain,
    val paymentMethod: String,
    val purpose: String,
    val recipient: RecipientDomain,
    val senderObjectId: String,
    val service: ServiceDomain,
    val sourceOfIncomeCode: String,
    val timestamp: String
)
