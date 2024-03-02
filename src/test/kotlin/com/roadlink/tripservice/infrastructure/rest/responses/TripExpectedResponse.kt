package com.roadlink.tripservice.infrastructure.rest.responses

import com.fasterxml.jackson.annotation.JsonInclude

data class TripExpectedResponse(
    val id: String,
    val driver: String,
    val vehicle: String,
    val departure: TripPointExpectedResponse,
    @JsonInclude(JsonInclude.Include.ALWAYS)
    val meetingPoints: List<TripPointExpectedResponse>,
    val arrival: TripPointExpectedResponse,
    val availableSeats: Int,
)
