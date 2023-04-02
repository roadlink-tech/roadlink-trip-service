package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.infrastructure.rest.responses.TripPointResponse

object TripPointResponseMapper {
    fun map(tripPoint: TripPoint) =
        TripPointResponse(
            estimatedArrivalTime = tripPoint.estimatedArrivalTime.toEpochMilli(),
            address = AddressResponseMapper.map(tripPoint.address),
        )
}