package com.roadlink.tripservice.infrastructure.rest.responses

data class TripPointResponse(
    val estimatedArrivalTime: Long,
    val address: AddressResponse,
)