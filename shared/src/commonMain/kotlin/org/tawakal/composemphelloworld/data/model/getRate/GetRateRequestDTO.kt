package org.tawakal.composemphelloworld.data.model.getRate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRateRequestDTO(
    @SerialName("Amount")
    val amount: String,
    @SerialName("curCode")
    val curCode: String,
    @SerialName("receivingCountry")
    val receivingCountry: String,
    @SerialName("sendingCountry")
    val sendingCountry: String,
    @SerialName("service")
    val service: String
)