package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.responses.SearchTripExpectedResponse

object SearchTripResponseFactory {
    fun empty() =
        SearchTripExpectedResponse(tripPlans = emptyList())

    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        SearchTripExpectedResponse(tripPlans = listOf(
            TripPlanResponseFactory.avCabildo4853_virreyDelPino1800_avCabildo20(),
        ))
}