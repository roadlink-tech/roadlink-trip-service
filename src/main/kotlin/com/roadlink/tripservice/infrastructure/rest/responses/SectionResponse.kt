package com.roadlink.tripservice.infrastructure.rest.responses

data class SectionResponse(
    val departure: TripPointResponse,
    val arrival: TripPointResponse,
    val driver: String,
    val vehicle: String,
    val availableSeats: Int,
)