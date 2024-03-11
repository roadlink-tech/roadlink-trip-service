package com.roadlink.tripservice.infrastructure.rest.trip_search.mapper

import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse

object SearchTripResponseMapper {
    fun map(tripSearchPlanResults: List<TripSearchPlanResult>) =
        SearchTripResponse(
            tripPlans = tripSearchPlanResults.map { TripPlanResponseMapper.map(it) }
        )
}