package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse
import com.roadlink.tripservice.usecases.trip.TripFactory

object SearchTripResponseFactory {
    fun empty() =
        SearchTripResponse(tripPlans = emptyList())

    fun avCabildo4853_virreyDelPino1800_avCabildo20(
        avCabildo4853_virreyDelPino1800_tripId: String = TripFactory.avCabildo_id,
        virreyDelPino1800_avCabildo20_tripId: String = TripFactory.avCabildo_id
    ) =
        SearchTripResponse(
            tripPlans = listOf(
                TripPlanResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(
                    avCabildo4853_virreyDelPino1800_tripId = avCabildo4853_virreyDelPino1800_tripId,
                    virreyDelPino1800_avCabildo20_tripId = virreyDelPino1800_avCabildo20_tripId
                ),
            )
        )
}