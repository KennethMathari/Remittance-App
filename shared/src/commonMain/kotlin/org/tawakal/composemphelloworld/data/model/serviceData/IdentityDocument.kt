package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdentityDocument(
    @SerialName("countryRegion")
    val countryRegion: String,
    @SerialName("dateOfBirth")
    val dateOfBirth: String,
    @SerialName("dateOfExpiration")
    val dateOfExpiration: String,
    @SerialName("dateOfIssue")
    val dateOfIssue: String,
    @SerialName("documentNumber")
    val documentNumber: String,
    @SerialName("identityCode")
    val identityCode: String,
    @SerialName("identityDescription")
    val identityDescription: String
)