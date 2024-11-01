package org.tawakal.composemphelloworld.domain.model.listRecipients

data class TransactionDomain(
    val balance: Double,
    val fee: Double,
    val fulfillmentTimestamp: String,
    val paymentService: String,
    val rate: Double,
    val recipientAmount: Double,
    val recipientCurrency: String,
    val senderCurrency: String,
    val service: ServiceDomain,
    val status: String,
    val timestamp: String,
    val txnId: String
)
