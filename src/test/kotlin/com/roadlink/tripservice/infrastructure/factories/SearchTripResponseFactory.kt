package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse

object SearchTripResponseFactory {
    fun empty() =
        SearchTripResponse(tripPlans = emptyList())

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        SearchTripResponse(tripPlans = listOf(
            TripPlanResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(),
        ))
}