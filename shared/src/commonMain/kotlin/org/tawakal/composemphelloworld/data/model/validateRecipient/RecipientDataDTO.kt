package com.example.myapplication.data.model.externaluser


import kotlinx.serialization.Serializable

@Serializable
data class RecipientDataDTO(
    val country: String? = null,
    val displayName: String? = null,
    val firstName: String,
    val id: String? = null,
    val lastName: String,
    val email: String? = null,
    val phone: String? = null,
    val usageLocation: String? = null
)
