package com.roadlink.tripservice.infrastructure.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class CreateTripExpectedRequest(
    @JsonProperty(value = "driver") val driver: String,
    @JsonProperty(value = "vehicle") val vehicle: String,
    @JsonProperty(value = "departure") val departure: TripPointExpectedRequest,
    @JsonProperty(value = "arrival") val arrival: TripPointExpectedRequest,
    @JsonProperty(value = "meeting_points") @JsonInclude(JsonInclude.Include.ALWAYS) val meetingPoints: List<TripPointExpectedRequest>,
    @JsonProperty(value = "available_seats") val availableSeats: Int,
)
