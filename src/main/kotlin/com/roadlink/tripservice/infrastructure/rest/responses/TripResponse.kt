package com.roadlink.tripservice.infrastructure.rest.responses

import com.fasterxml.jackson.annotation.JsonInclude

data class TripResponse(
    val id: String,
    val driver: String,
    val vehicle: String,
    val departure: TripPointResponse,
    @JsonInclude(JsonInclude.Include.ALWAYS)
    val meetingPoints: List<TripPointResponse>,
    val arrival: TripPointResponse,
    val availableSeats: Int,
)
