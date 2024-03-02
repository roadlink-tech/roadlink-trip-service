package com.roadlink.tripservice.infrastructure.rest.responses

data class TripPointExpectedResponse(
    val estimatedArrivalTime: Long,
    val address: AddressExpectedResponse,
)
