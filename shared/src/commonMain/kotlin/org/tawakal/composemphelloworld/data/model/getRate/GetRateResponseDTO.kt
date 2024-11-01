package org.tawakal.composemphelloworld.data.model.getRate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRateResponseDTO(
    @SerialName("data")
    val `data`: GetRateResponseDataDTO,
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: String,
    @SerialName("statusCode")
    val statusCode: Int,
    @SerialName("timeStamp")
    val timeStamp: String
)