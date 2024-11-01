package org.tawakal.composemphelloworld.data.model.getRate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRateResponseDataDTO(
    @SerialName("recipient")
    val recipient: RateDTO
)