package com.roadlink.tripservice.infrastructure.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.usecases.CreateTrip

data class CreateTripRequest(
    @JsonProperty(value = "driver") val driver: String,
    @JsonProperty(value = "vehicle") val vehicle: String,
    @JsonProperty(value = "departure") val departure: TripPointRequest,
    @JsonProperty(value = "arrival") val arrival: TripPointRequest,
    @JsonProperty(value = "meeting_points") val meetingPoints: List<TripPointRequest>,
    @JsonProperty(value = "available_seats") val availableSeats: Int,
) {
    fun toDomain(): CreateTrip.Input {
        return CreateTrip.Input(
            driver = this.driver,
            vehicle = this.vehicle,
            departure = this.departure.toModel(),
            arrival = this.arrival.toModel(),
            meetingPoints = meetingPoints.map { it.toModel() },
            availableSeats = availableSeats
        )
    }
}