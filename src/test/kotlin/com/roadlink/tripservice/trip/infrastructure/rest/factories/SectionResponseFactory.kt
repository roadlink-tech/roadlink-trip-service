package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.infrastructure.rest.responses.SectionExpectedResponse

object SectionResponseFactory {
    fun avCabildo4853_virreyDelPino1800() =
        SectionExpectedResponse(
            departure = TripPointResponseFactory.avCabildo_4853(),
            arrival = TripPointResponseFactory.virreyDelPino1800(),
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino1800_avCabildo20() =
        SectionExpectedResponse(
            departure = TripPointResponseFactory.virreyDelPino1800(),
            arrival = TripPointResponseFactory.avCabildo_20(),
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )
}