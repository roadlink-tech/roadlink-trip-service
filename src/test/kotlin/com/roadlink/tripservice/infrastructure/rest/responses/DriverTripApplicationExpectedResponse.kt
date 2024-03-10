package com.roadlink.tripservice.infrastructure.rest.responses

data class DriverTripApplicationExpectedResponse(
    val tripLegSolicitudeId: String,
    val passenger: PassengerResultExpectedResponse,
    val status: TripApplicationStatusExpectedResponse,
    val addressJoinStart: AddressExpectedResponse,
    val addressJoinEnd: AddressExpectedResponse,
)
