package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.roadlink.tripservice.infrastructure.rest.common.address.AddressResponse
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripLegSolicitudeStatusResponse

data class DriverTripLegSolicitudeResponse(
    val tripLegSolicitudeId: String,
    val passenger: PassengerResultResponse,
    val status: TripLegSolicitudeStatusResponse,
    val addressJoinStart: AddressResponse,
    val addressJoinEnd: AddressResponse,
)
