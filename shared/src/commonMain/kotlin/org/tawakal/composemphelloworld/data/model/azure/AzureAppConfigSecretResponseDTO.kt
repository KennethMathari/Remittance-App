package org.tawakal.composemphelloworld.data.model.azure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AzureAppConfigSecretResponseDTO(
    @SerialName("value")
    val value: String
)
