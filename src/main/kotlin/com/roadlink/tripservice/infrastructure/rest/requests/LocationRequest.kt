package com.roadlink.tripservice.infrastructure.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class LocationRequest(
    @JsonProperty(value = "latitude") val latitude: Double,
    @JsonProperty(value = "longitude") val longitude: Double
)