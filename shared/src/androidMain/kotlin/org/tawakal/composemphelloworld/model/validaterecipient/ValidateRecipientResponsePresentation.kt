package org.tawakal.composemphelloworld.model.validaterecipient

data class ValidateRecipientResponsePresentation(
    val validateRecipientData: ValidateRecipientDataPresentation,
    val source: String,
    val status: String,
    val statusCode: Int,
    val timeStamp: String
)
