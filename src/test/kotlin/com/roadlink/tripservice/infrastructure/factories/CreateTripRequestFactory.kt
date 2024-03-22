package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip.request.CreateTripRequest
import com.roadlink.tripservice.usecases.common.InstantFactory

object CreateTripRequestFactory {
    fun avCabildo() =
        CreateTripRequest(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointRequestFactory.avCabildo_4853(),
            meetingPoints = emptyList(),
            arrival = TripPointRequestFactory.avCabildo_20(),
            availableSeats = 4,
        )

    fun avCabildo_invalidTimeRange() =
        CreateTripRequest(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointRequestFactory.avCabildo_4853(),
            meetingPoints = emptyList(),
            arrival = TripPointRequestFactory.avCabildo_20(
                estimatedArrivalTime = InstantFactory.october15_7hs(),
            ),
            availableSeats = 4,
        )

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        CreateTripRequest(
            driver = "John Smith",
            vehicle = "Ford mustang",
            departure = TripPointRequestFactory.avCabildo_4853(),
            meetingPoints = listOf(TripPointRequestFactory.virreyDelPino_1800()),
            arrival = TripPointRequestFactory.avCabildo_20(),
            availableSeats = 5,
        )
}