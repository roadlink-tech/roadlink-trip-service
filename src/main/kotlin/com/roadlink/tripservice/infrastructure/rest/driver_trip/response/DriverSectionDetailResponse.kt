package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponse

data class DriverSectionDetailResponse(
    val sectionId: String,
    val departure: TripPointResponse,
    val arrival: TripPointResponse,
    val occupiedSeats: Int,
    val availableSeats: Int,
    @JsonInclude(JsonInclude.Include.ALWAYS)
    val passengers: List<PassengerResultResponse>,
)