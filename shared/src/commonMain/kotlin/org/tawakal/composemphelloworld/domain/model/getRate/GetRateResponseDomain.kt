package org.tawakal.composemphelloworld.domain.model.getRate


data class GetRateResponseDomain(
    val `data`: GetRateResponseDataDomain,
    val message: String,
    val status: String,
    val statusCode: Int,
    val timeStamp: String
)
