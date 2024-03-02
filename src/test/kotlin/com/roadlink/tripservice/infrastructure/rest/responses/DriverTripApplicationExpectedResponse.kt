package com.roadlink.tripservice.infrastructure.rest.responses

data class DriverTripApplicationExpectedResponse(
    val tripApplicationId: String,
    val passenger: PassengerResultExpectedResponse,
    val applicationStatus: TripApplicationStatusExpectedResponse,
    val addressJoinStart: AddressExpectedResponse,
    val addressJoinEnd: AddressExpectedResponse,
)
