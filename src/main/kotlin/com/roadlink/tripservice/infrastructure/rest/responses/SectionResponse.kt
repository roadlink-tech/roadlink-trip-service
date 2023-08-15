package com.roadlink.tripservice.infrastructure.rest.responses

data class SectionResponse(
    val id: String,
    val tripId: String,
    val departure: TripPointResponse,
    val arrival: TripPointResponse,
    val driver: String,
    val vehicle: String,
    val availableSeats: Int,
)