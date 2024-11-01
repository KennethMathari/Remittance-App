package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceOfIncome(
    @SerialName("sourceCode")
    val sourceCode: String,
    @SerialName("sourceDescription")
    val sourceDescription: String
)