package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.infrastructure.rest.responses.SearchTripResponse

object SearchTripResponseMapper {
    fun map(tripPlans: List<TripPlan>) =
        SearchTripResponse(
            tripPlans = tripPlans.map { TripPlanResponseMapper.map(it) }
        )
}