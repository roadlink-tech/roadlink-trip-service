package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.infrastructure.rest.responses.TripExpectedResponse

object TripResponseFactory {
    fun avCabildo() =
        TripExpectedResponse(
            id = TripFactory.avCabildo_id,
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointResponseFactory.avCabildo_4853(),
            meetingPoints = emptyList(),
            arrival = TripPointResponseFactory.avCabildo_20(),
            availableSeats = 4,
        )

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        TripExpectedResponse(
            id = TripFactory.avCabildo4853_virreyDelPino1800_avCabildo20_id,
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointResponseFactory.avCabildo_4853(),
            meetingPoints = listOf(TripPointResponseFactory.virreyDelPino1800()),
            arrival = TripPointResponseFactory.avCabildo_20(),
            availableSeats = 5,
        )
}