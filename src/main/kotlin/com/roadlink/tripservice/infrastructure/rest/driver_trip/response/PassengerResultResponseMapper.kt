package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.roadlink.tripservice.domain.*

object PassengerResultResponseMapper {
    fun map(passengerResult: PassengerResult): PassengerResultResponse =
        when (passengerResult) {
            is Passenger ->
                PassengerResponse(
                    id = passengerResult.id,
                    fullName = passengerResult.fullName,
                    rating = when (passengerResult.rating) {
                        is Rated -> RatedResponse(rating = passengerResult.rating.rating)
                        NotBeenRated -> NotBeenRatedResponse()
                    },
                )

            is PassengerNotExists ->
                PassengerNotExistsResponse(id = passengerResult.id)
        }
}

data class RatedResponse(
    override val type: RatingResultResponseType = RatingResultResponseType.RATED,
    val rating: Double,
) : RatingResultResponse(type)

data class NotBeenRatedResponse(
    override val type: RatingResultResponseType = RatingResultResponseType.NOT_BEEN_RATED,
) : RatingResultResponse(type)

sealed class RatingResultResponse(open val type: RatingResultResponseType)
enum class RatingResultResponseType {
    RATED,
    NOT_BEEN_RATED,
}