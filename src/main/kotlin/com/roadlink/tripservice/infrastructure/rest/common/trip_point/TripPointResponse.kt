package com.roadlink.tripservice.infrastructure.rest.common.trip_point

import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponse

data class TripPointResponse(
    val estimatedArrivalTime: Long,
    val address: AddressResponse,
) {
    companion object {
        fun from(tripPoint: TripPoint): TripPointResponse {
            return TripPointResponse(
                estimatedArrivalTime = tripPoint.estimatedArrivalTime.toEpochMilli(),
                address = AddressResponse.from(tripPoint.address)
            )
        }
    }
}