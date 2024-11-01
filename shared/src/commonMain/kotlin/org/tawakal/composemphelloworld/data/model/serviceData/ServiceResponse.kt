package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponse(
    @SerialName("countries")
    val countries: List<Country>,
    @SerialName("purpose")
    val purpose: List<Purpose>,
    @SerialName("relationship")
    val relationship: List<Relationship>,
    @SerialName("sourceOfIncome")
    val sourceOfIncome: List<SourceOfIncome>,
    @SerialName("status")
    val status: String,
    @SerialName("statusCode")
    val statusCode: String,
    @SerialName("timeStamp")
    val timeStamp: String
)