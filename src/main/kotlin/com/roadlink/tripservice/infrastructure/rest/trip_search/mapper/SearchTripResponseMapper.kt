package com.roadlink.tripservice.infrastructure.rest.trip_search.mapper

import com.roadlink.tripservice.domain.trip_search.TripPlan
import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse

object SearchTripResponseMapper {
    fun map(tripPlans: List<TripPlan>) =
        SearchTripResponse(
            tripPlans = tripPlans.map { TripPlanResponseMapper.map(it) }
        )
}