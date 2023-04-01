package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.infrastructure.rest.responses.TripPointResponse

object TripPointResponseMapper {
    fun map(tripPoint: TripPoint) =
        TripPointResponse(
            location = LocationResponseMapper.map(tripPoint.location),
            at = tripPoint.at.toEpochMilli(),
            formatted = tripPoint.formatted,
            street = tripPoint.street,
            city = tripPoint.city,
            country = tripPoint.country,
            housenumber = tripPoint.housenumber,
        )
}