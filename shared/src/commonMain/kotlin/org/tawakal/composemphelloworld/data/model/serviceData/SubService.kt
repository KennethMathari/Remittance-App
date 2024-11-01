package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubService(
    @SerialName("serviceCode")
    val serviceCode: String,
    @SerialName("serviceName")
    val serviceName: String
)