package com.roadlink.tripservice.infrastructure.rest.trip.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponse

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
