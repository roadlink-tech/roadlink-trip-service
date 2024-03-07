package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponse
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripApplicationStatusResponse

data class DriverTripApplicationResponse(
    val tripApplicationId: String,
    val passenger: PassengerResultResponse,
    val applicationStatus: TripApplicationStatusResponse,
    val addressJoinStart: AddressResponse,
    val addressJoinEnd: AddressResponse,
)
