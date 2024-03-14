package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.domain.driver_trip.Passenger
import com.roadlink.tripservice.domain.driver_trip.PassengerNotExists
import com.roadlink.tripservice.domain.driver_trip.PassengerResult


object PassengerResultResponseMapper {
    fun map(passengerResult: PassengerResult): PassengerResultResponse =
        when (passengerResult) {
            is Passenger ->
                PassengerResponse(
                    id = passengerResult.id,
                    fullName = passengerResult.fullName,
                    rating = if (passengerResult.hasBeenRated) {
                        RatedResponse(rating = passengerResult.score)
                    } else {
                        NotBeenRatedResponse()
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