package com.roadlink.tripservice.infrastructure.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.infrastructure.rest.trip.request.AddressRequest

data class TripPointExpectedRequest(
    @JsonProperty(value = "estimated_arrival_time") val estimatedArrivalTime: String,
    @JsonProperty(value = "address") val address: AddressRequest,
)
