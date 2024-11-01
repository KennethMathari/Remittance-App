package org.tawakal.composemphelloworld.data.model.createTransaction


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Service(
    @SerialName("cityCode")
    val cityCode: String,
    @SerialName("paymentMode")
    val paymentMode: String,
    @SerialName("serviceCode")
    val serviceCode: String,
    @SerialName("serviceName")
    val serviceName: String
)