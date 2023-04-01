package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.domain.TripFactory
import com.roadlink.tripservice.trip.infrastructure.rest.responses.TripExpectedResponse

object TripResponseFactory {
    fun avCabildo() =
        TripExpectedResponse(
            id = TripFactory.avCabildoId,
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointResponseFactory.avCabildo_4853(),
            meetingPoints = emptyList(),
            arrival = TripPointResponseFactory.avCabildo_20(),
            availableSeats = 4,
        )

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        TripExpectedResponse(
            id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20,
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointResponseFactory.avCabildo_4853(),
            meetingPoints = listOf(TripPointResponseFactory.virreyDelPino1800()),
            arrival = TripPointResponseFactory.avCabildo_20(),
            availableSeats = 5,
        )
}