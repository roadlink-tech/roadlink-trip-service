package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.infrastructure.rest.responses.SectionResponse

object SectionResponseMapper {
    fun map(section: Section) =
        SectionResponse(
            departure = TripPointResponseMapper.map(section.departure),
            arrival = TripPointResponseMapper.map(section.arrival),
            driver = section.driver,
            vehicle = section.vehicle,
            availableSeats = section.availableSeats,
        )
}