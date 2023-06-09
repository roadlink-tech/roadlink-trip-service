package com.roadlink.tripservice.infrastructure.rest.responses

import com.fasterxml.jackson.annotation.JsonInclude

data class DriverSectionDetailResponse(
    val sectionId: String,
    val departure: TripPointResponse,
    val arrival: TripPointResponse,
    val occupiedSeats: Int,
    val availableSeats: Int,
    @JsonInclude(JsonInclude.Include.ALWAYS)
    val passengers: List<PassengerResultResponse>,
)