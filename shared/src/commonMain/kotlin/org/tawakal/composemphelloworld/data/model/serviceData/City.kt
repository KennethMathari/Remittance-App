package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class City(
    @SerialName("cityCode")
    val cityCode: String,
    @SerialName("cityName")
    val cityName: String
)