package com.roadlink.tripservice.infrastructure.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.infrastructure.rest.trip.request.TripPointRequest

data class CreateTripExpectedRequest(
    @JsonProperty(value = "driver") val driver: String,
    @JsonProperty(value = "vehicle") val vehicle: String,
    @JsonProperty(value = "departure") val departure: TripPointRequest,
    @JsonProperty(value = "arrival") val arrival: TripPointRequest,
    @JsonProperty(value = "meeting_points") @JsonInclude(JsonInclude.Include.ALWAYS) val meetingPoints: List<TripPointRequest>,
    @JsonProperty(value = "available_seats") val availableSeats: Int,
)
