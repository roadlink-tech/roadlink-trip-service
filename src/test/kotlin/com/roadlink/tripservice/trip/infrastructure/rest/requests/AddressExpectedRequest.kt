package com.roadlink.tripservice.trip.infrastructure.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class AddressExpectedRequest(
    @JsonProperty(value = "street") val street: String,
    @JsonProperty(value = "city") val city: String,
    @JsonProperty(value = "country") val country: String,
    @JsonProperty(value = "house_number") val houseNumber: String,
)
