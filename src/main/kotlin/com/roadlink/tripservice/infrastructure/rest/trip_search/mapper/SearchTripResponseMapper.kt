package com.roadlink.tripservice.infrastructure.rest.trip_search.mapper

import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.infrastructure.rest.trip_plan.mapper.TripPlanResponseMapper
import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse

object SearchTripResponseMapper {
    fun map(tripPlans: List<TripPlan>) =
        SearchTripResponse(
            tripPlans = tripPlans.map { TripPlanResponseMapper.map(it) }
        )
}