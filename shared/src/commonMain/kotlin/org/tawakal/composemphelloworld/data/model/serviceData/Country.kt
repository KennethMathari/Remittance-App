package org.tawakal.composemphelloworld.data.model.serviceData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName("city")
    val city: List<City>,
    @SerialName("countryCode")
    val countryCode: String,
    @SerialName("countryName")
    val countryName: String,
    @SerialName("identityDocument")
    val identityDocument: List<IdentityDocument>,
    @SerialName("service")
    val service: List<Service>
)