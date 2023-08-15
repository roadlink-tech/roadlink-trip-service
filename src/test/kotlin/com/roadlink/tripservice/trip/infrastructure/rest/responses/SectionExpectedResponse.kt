package com.roadlink.tripservice.trip.infrastructure.rest.responses

data class SectionExpectedResponse(
    val id: String,
    val tripId: String,
    val departure: TripPointExpectedResponse,
    val arrival: TripPointExpectedResponse,
    val driver: String,
    val vehicle: String,
    val availableSeats: Int,
)
