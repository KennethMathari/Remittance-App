package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
    @SerialName("relationshipCode")
    val relationshipCode: String,
    @SerialName("relationshipDescription")
    val relationshipDescription: String,
    @SerialName("relationshipTitle")
    val relationshipTitle: String
)