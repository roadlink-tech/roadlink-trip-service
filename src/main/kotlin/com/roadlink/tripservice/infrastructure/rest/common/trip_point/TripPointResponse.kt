package com.roadlink.tripservice.infrastructure.rest.common.trip_point

import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponse

data class TripPointResponse(
    val estimatedArrivalTime: Long,
    val address: AddressResponse,
)