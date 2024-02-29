package com.roadlink.tripservice.infrastructure.rest.trip_plan.mapper

import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponseMapper
import com.roadlink.tripservice.infrastructure.rest.trip_plan.response.SectionResponse
import com.roadlink.tripservice.infrastructure.rest.trip_plan.response.TripPlanResponse

object SectionResponseMapper {
    fun map(section: Section) =
        SectionResponse(
            id = section.id,
            tripId = section.tripId.toString(),
            departure = TripPointResponseMapper.map(section.departure),
            arrival = TripPointResponseMapper.map(section.arrival),
            driver = section.driver,
            vehicle = section.vehicle,
            availableSeats = section.initialAmountOfSeats,
        )
}

object TripPlanResponseMapper {
    fun map(tripPlan: TripPlan) =
        TripPlanResponse(
            sections = tripPlan.sections.map { SectionResponseMapper.map(it) }
        )
}