package org.tawakal.composemphelloworld.domain.model.validateRecipient


data class RecipientDataDomain(
    val country: String,
    val displayName: String,
    val firstName: String,
    val id: String,
    val lastName: String,
    val phone: String,
    val usageLocation: String,
    val email: String
)
