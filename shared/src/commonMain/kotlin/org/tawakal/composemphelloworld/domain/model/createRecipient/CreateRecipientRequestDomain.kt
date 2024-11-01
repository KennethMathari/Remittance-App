package org.tawakal.composemphelloworld.domain.model.createRecipient

data class CreateRecipientRequestDomain(
    val country: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val relationship: String,
    val usageLocation: String
)
