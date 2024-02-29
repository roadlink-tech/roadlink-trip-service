package com.roadlink.tripservice.infrastructure.rest.common.trip_point

import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponseMapper

object TripPointResponseMapper {
    fun map(tripPoint: TripPoint) =
        TripPointResponse(
            estimatedArrivalTime = tripPoint.estimatedArrivalTime.toEpochMilli(),
            address = AddressResponseMapper.map(tripPoint.address),
        )
}