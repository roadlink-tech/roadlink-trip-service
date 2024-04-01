package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.roadlink.tripservice.domain.driver_trip.Passenger
import com.roadlink.tripservice.domain.driver_trip.PassengerNotExists
import com.roadlink.tripservice.domain.driver_trip.PassengerResult
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponse.PassengerNotExistsResponse
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponse.PassengerResponse
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.ScoreResultResponse.NotBeenScoredResponse
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.ScoreResultResponse.ScoreResponse
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.ScoreResultResponseType.NOT_BEEN_SCORED
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.ScoreResultResponseType.SCORED


object PassengerResultResponseMapper {
    fun map(passengerResult: PassengerResult): PassengerResultResponse =
        when (passengerResult) {
            is Passenger ->
                PassengerResponse(
                    id = passengerResult.id,
                    fullName = passengerResult.fullName,
                    score = if (passengerResult.hasBeenScored) {
                        ScoreResponse(score = passengerResult.score)
                    } else {
                        NotBeenScoredResponse()
                    },
                    profilePhotoUrl = passengerResult.profilePhotoUrl
                )

            is PassengerNotExists ->
                PassengerNotExistsResponse(id = passengerResult.id)
        }
}

sealed class ScoreResultResponse(open val type: ScoreResultResponseType) {
    data class ScoreResponse(
        override val type: ScoreResultResponseType = SCORED,
        val score: Double,
    ) : ScoreResultResponse(type)

    data class NotBeenScoredResponse(
        override val type: ScoreResultResponseType = NOT_BEEN_SCORED,
    ) : ScoreResultResponse(type)
}

enum class ScoreResultResponseType {
    SCORED,
    NOT_BEEN_SCORED,
}