package com.roadlink.tripservice.infrastructure.rest.responses

enum class PassengerResultResponseType {
    PASSENGER,
    PASSENGER_NOT_EXISTS,
}

sealed class PassengerResultResponse(open val type: PassengerResultResponseType)

data class PassengerNotExistsResponse(
    override val type: PassengerResultResponseType = PassengerResultResponseType.PASSENGER_NOT_EXISTS,
    val id: String,
) : PassengerResultResponse(type)

data class PassengerResponse(
    override val type: PassengerResultResponseType = PassengerResultResponseType.PASSENGER,
    val id: String,
    val fullName: String,
    val rating: RatingResultResponse,
) : PassengerResultResponse(type)
