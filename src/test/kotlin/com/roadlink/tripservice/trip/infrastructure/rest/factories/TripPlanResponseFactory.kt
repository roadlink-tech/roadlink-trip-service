package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.infrastructure.rest.responses.TripPlanExpectedResponse

object TripPlanResponseFactory {
    fun avCabildo4853_virreyDelPino1800_avCabildo20() =
        TripPlanExpectedResponse(sections = listOf(
            SectionResponseFactory.avCabildo4853_virreyDelPino1800(),
            SectionResponseFactory.virreyDelPino1800_avCabildo20(),
        ))
}