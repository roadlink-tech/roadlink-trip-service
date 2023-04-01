package com.roadlink.tripservice.trip.infrastructure.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class LocationExpectedRequest(
    @JsonProperty(value = "latitude") val latitude: Double,
    @JsonProperty(value = "longitude") val longitude: Double,
)
