package org.tawakal.composemphelloworld.domain.model.createRecipient

data class RecipientDomain(
    val country: String,
    val displayName: String,
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val phone: String,
    val usageLocation: String
)

