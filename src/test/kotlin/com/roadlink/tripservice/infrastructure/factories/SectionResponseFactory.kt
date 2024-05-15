package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SectionResponse
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory

object SectionResponseFactory {
    fun avCabildo4853_virreyDelPino1800(tripId: String = TripFactory.avCabildo_id) =
        SectionResponse(
            id = SectionFactory.avCabildo4853_virreyDelPino1800_id,
            tripId = tripId,
            departure = TripPointResponseFactory.avCabildo_4853(),
            arrival = TripPointResponseFactory.virreyDelPino1800(),
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino1800_avCabildo20(tripId: String = TripFactory.avCabildo_id) =
        SectionResponse(
            id = SectionFactory.virreyDelPino1800_avCabildo20_id,
            tripId = tripId,
            departure = TripPointResponseFactory.virreyDelPino1800(),
            arrival = TripPointResponseFactory.avCabildo_20(),
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )
}