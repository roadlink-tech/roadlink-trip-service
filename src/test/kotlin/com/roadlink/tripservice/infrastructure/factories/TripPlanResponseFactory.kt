package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.trip_search.response.TripPlanResponse

object TripPlanResponseFactory {
    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        TripPlanResponse(sections = listOf(
            SectionResponseFactory.avCabildo4853_virreyDelPino1800(),
            SectionResponseFactory.virreyDelPino1800_avCabildo20(),
        ))
}