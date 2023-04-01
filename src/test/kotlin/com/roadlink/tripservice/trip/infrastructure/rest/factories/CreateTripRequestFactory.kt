package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.infrastructure.rest.requests.CreateTripExpectedRequest

object CreateTripRequestFactory {
    fun avCabildo() =
        CreateTripExpectedRequest(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointRequestFactory.avCabildo_4853(),
            meetingPoints = emptyList(),
            arrival = TripPointRequestFactory.avCabildo_20(),
            availableSeats = 4,
        )

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        CreateTripExpectedRequest(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointRequestFactory.avCabildo_4853(),
            meetingPoints = listOf(TripPointRequestFactory.virreyDelPino_1800()),
            arrival = TripPointRequestFactory.avCabildo_20(),
            availableSeats = 5,
        )
}