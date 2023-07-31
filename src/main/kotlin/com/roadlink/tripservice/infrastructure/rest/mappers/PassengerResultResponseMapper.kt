package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.infrastructure.rest.responses.*

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