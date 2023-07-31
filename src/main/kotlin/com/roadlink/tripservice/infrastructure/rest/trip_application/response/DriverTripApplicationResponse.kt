package com.roadlink.tripservice.infrastructure.rest.trip_application.response

import com.roadlink.tripservice.infrastructure.rest.responses.AddressResponse
import com.roadlink.tripservice.infrastructure.rest.responses.PassengerResultResponse

data class DriverTripApplicationResponse(
    val tripApplicationId: String,
    val passenger: PassengerResultResponse,
    val applicationStatus: TripApplicationStatusResponse,
    val addressJoinStart: AddressResponse,
    val addressJoinEnd: AddressResponse,
)
