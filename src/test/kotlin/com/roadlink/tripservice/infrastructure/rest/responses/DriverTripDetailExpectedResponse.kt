package com.roadlink.tripservice.infrastructure.rest.responses

import com.fasterxml.jackson.annotation.JsonInclude
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.ScoreResultResponseType
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.ScoreResultResponseType.*
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.SeatsAvailabilityStatusResponse
import com.roadlink.tripservice.infrastructure.rest.responses.PassengerResultExpectedResponseType.PASSENGER

data class DriverTripDetailExpectedResponse(
    val tripId: String,
    val tripStatus: TripStatusExpectedResponse,
    val seatStatus: SeatsAvailabilityStatusResponse,
    val hasPendingApplications: Boolean,
    val sectionDetails: List<DriverSectionDetailExpectedResponse>
)

enum class TripStatusExpectedResponse { NOT_STARTED, IN_PROGRESS, FINISHED }


data class DriverSectionDetailExpectedResponse(
    val sectionId: String,
    val departure: TripPointExpectedResponse,
    val arrival: TripPointExpectedResponse,
    val occupiedSeats: Int,
    val availableSeats: Int,
    @JsonInclude(JsonInclude.Include.ALWAYS)
    val passengers: List<PassengerResultExpectedResponse>,
)

enum class PassengerResultExpectedResponseType {
    PASSENGER,
    PASSENGER_NOT_EXISTS,
}

sealed class PassengerResultExpectedResponse(open val type: PassengerResultExpectedResponseType)

data class PassengerExpectedResponse(
    override val type: PassengerResultExpectedResponseType = PASSENGER,
    val id: String,
    val fullName: String,
    val rating: ScoreResultExpectedResponse,
) : PassengerResultExpectedResponse(type)

sealed class ScoreResultExpectedResponse(open val type: ScoreResultResponseType) {
    data class ScoredExpectedResponse(
        override val type: ScoreResultResponseType = SCORED,
        val rating: Double,
    ) : ScoreResultExpectedResponse(type)

    data class NotBeenScoredExpectedResponse(
        override val type: ScoreResultResponseType = NOT_BEEN_SCORED,
    ) : ScoreResultExpectedResponse(type)

}

