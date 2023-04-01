package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.infrastructure.rest.responses.TripPlanResponse

object TripPlanResponseMapper {
    fun map(tripPlan: TripPlan) =
        TripPlanResponse(
            sections = tripPlan.sections.map { SectionResponseMapper.map(it) }
        )
}