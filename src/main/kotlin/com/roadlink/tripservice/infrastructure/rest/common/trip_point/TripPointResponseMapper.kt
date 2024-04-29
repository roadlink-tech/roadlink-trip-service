package com.roadlink.tripservice.infrastructure.rest.common.trip_point

import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponseMapper

// TODO do not use it. Use TripPoint.from() method
object TripPointResponseMapper {
    fun map(tripPoint: TripPoint) =
        TripPointResponse(
            estimatedArrivalTime = tripPoint.estimatedArrivalTime.toEpochMilli(),
            address = AddressResponseMapper.map(tripPoint.address),
        )
}