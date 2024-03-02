package com.roadlink.tripservice.infrastructure.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class AddressExpectedRequest(
    @JsonProperty(value = "location") val location: LocationExpectedRequest,
    @JsonProperty(value = "street") val street: String,
    @JsonProperty(value = "city") val city: String,
    @JsonProperty(value = "country") val country: String,
    @JsonProperty(value = "house_number") val houseNumber: String,
)
