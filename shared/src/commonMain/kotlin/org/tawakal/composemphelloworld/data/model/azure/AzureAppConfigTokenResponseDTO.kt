package org.tawakal.composemphelloworld.data.model.azure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AzureAppConfigTokenResponseDTO(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int,
    @SerialName("ext_expires_in")
    val extExpiresIn: Int,
    @SerialName("token_type")
    val tokenType: String
)
