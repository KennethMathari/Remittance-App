package org.tawakal.composemphelloworld.data.model.getRate


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RateDTO(
    @SerialName("maximumAmount")
    val maximumAmount: Double,
    @SerialName("minimumAmount")
    val minimumAmount: Double,
    @SerialName("payoutAmount")
    val payoutAmount: Double,
    @SerialName("payoutCurrency")
    val payoutCurrency: String,
    @SerialName("payoutFee")
    val payoutFee: Double,
    @SerialName("payoutRate")
    val payoutRate: Double
)