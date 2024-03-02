package com.roadlink.tripservice.infrastructure.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class TripPointExpectedRequest(
    @JsonProperty(value = "estimated_arrival_time") val estimatedArrivalTime: String,
    @JsonProperty(value = "address") val address: AddressExpectedRequest,
)
