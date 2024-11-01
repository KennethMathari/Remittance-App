package org.tawakal.composemphelloworld.domain.model.listRecipients


data class ListRecipientResponseDomain(
    val `data`: DataDomain,
    val message: String,
    val status: String,
    val statusCode: Int,
    val timeStamp: String
)
