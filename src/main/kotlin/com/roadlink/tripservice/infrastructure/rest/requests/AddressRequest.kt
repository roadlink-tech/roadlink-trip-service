package com.roadlink.tripservice.infrastructure.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class AddressRequest(
    @JsonProperty(value = "street") val street: String,
    @JsonProperty(value = "city") val city: String,
    @JsonProperty(value = "country") val country: String,
    @JsonProperty(value = "house_number") val houseNumber: String
) {
    override fun toString(): String {
        return "$street $houseNumber, $city"
    }
}