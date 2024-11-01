package org.tawakal.composemphelloworld.domain.model.listRecipients

data class RecipientDomain(
    val firstName: String,
    val lastName: String,
    val mobilePhone: String,
    val objectId: String,
    val transactions: List<TransactionDomain>,
    val usageLocation: String
)
