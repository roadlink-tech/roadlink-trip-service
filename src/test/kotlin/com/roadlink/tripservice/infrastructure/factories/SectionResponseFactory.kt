package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SectionResponse

object SectionResponseFactory {
    fun avCabildo4853_virreyDelPino1800() =
        SectionResponse(
            id = SectionFactory.avCabildo4853_virreyDelPino1800_id,
            tripId = TripFactory.avCabildo_id,
            departure = TripPointResponseFactory.avCabildo_4853(),
            arrival = TripPointResponseFactory.virreyDelPino1800(),
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )

    fun virreyDelPino1800_avCabildo20() =
        SectionResponse(
            id = SectionFactory.virreyDelPino1800_avCabildo20_id,
            tripId = TripFactory.avCabildo_id,
            departure = TripPointResponseFactory.virreyDelPino1800(),
            arrival = TripPointResponseFactory.avCabildo_20(),
            driver = "John Smith",
            vehicle = "Ford mustang",
            availableSeats = 4,
        )
}