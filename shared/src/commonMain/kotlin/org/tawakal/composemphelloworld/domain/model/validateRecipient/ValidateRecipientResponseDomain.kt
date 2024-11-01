package org.tawakal.composemphelloworld.domain.model.validateRecipient



data class ValidateRecipientResponseDomain(
    val validateRecipientData: ValidateRecipientDataDomain,
    val source: String,
    val status: String,
    val statusCode: Int,
    val timeStamp: String
)
