package org.tawakal.composemphelloworld.domain.model.getRate

data class RateDomain(
    val maximumAmount: Double,
    val minimumAmount: Double,
    var payoutAmount: Double,
    val payoutCurrency: String,
    val payoutFee: Double,
    val payoutRate: Double
)
