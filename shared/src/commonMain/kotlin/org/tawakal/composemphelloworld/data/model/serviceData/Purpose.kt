package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Purpose(
    @SerialName("purposeCode")
    val purposeCode: String,
    @SerialName("purposeDescription")
    val purposeDescription: String
)