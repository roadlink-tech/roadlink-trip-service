package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponseType.PASSENGER
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponseType.PASSENGER_NOT_EXISTS

enum class PassengerResultResponseType {
    PASSENGER,
    PASSENGER_NOT_EXISTS,
}

sealed class PassengerResultResponse(open val type: PassengerResultResponseType) {
    data class PassengerNotExistsResponse(
        override val type: PassengerResultResponseType = PASSENGER_NOT_EXISTS,
        val id: String,
    ) : PassengerResultResponse(type)

    data class PassengerResponse(
        override val type: PassengerResultResponseType = PASSENGER,
        val id: String,
        val fullName: String,
        val rating: ScoreResultResponse,
    ) : PassengerResultResponse(type)
}


