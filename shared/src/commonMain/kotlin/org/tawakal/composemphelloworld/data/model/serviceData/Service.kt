package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Service(
    @SerialName("content")
    val content: String,
    @SerialName("subService")
    val subService: List<SubService>,
    @SerialName("title")
    val title: String
)