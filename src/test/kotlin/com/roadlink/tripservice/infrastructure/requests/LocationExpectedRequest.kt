package com.roadlink.tripservice.infrastructure.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class LocationExpectedRequest(
    @JsonProperty(value = "latitude") val latitude: Double,
    @JsonProperty(value = "longitude") val longitude: Double,
)
