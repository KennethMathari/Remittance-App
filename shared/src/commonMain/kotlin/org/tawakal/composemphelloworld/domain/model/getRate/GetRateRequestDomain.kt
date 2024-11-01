package org.tawakal.composemphelloworld.domain.model.getRate


data class GetRateRequestDomain(
    val amount: String,
    val curCode: String,
    val receivingCountry: String,
    val sendingCountry: String,
    val service: String
)
