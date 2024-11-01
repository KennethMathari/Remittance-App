package org.tawakal.composemphelloworld.data.model.azure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigSettingDTO(
    @SerialName("key") val key: String, @SerialName("value") val value: String
)
