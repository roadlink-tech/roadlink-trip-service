package com.roadlink.tripservice.trip.infrastructure.rest.responses

data class TripPointExpectedResponse(
    val estimatedArrivalTime: Long,
    val address: AddressExpectedResponse,
)
