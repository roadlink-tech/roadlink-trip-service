package com.roadlink.tripservice.infrastructure.rest.responses

import com.fasterxml.jackson.annotation.JsonInclude
import com.roadlink.tripservice.infrastructure.rest.responses.PassengerResultExpectedResponseType.PASSENGER
import com.roadlink.tripservice.infrastructure.rest.responses.PassengerResultExpectedResponseType.PASSENGER_NOT_EXISTS
import com.roadlink.tripservice.infrastructure.rest.responses.RatingResultExpectedResponseType.NOT_BEEN_RATED
import com.roadlink.tripservice.infrastructure.rest.responses.RatingResultExpectedResponseType.RATED

data class DriverTripDetailExpectedResponse(
    val tripId: String,
    val tripStatus: TripStatusExpectedResponse,
    val seatStatus: SeatsAvailabilityStatusExpectedResponse,
    val hasPendingApplications: Boolean,
    val sectionDetails: List<DriverSectionDetailExpectedResponse>
)

enum class TripStatusExpectedResponse { NOT_STARTED, IN_PROGRESS, FINISHED }

enum class SeatsAvailabilityStatusExpectedResponse { ALL_SEATS_AVAILABLE, SOME_SEATS_AVAILABLE, NO_SEATS_AVAILABLE }

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
    val rating: RatingResultExpectedResponse,
) : PassengerResultExpectedResponse(type)

data class PassengerNotExistsExpectedResponse(
    override val type: PassengerResultExpectedResponseType = PASSENGER_NOT_EXISTS,
    val id: String,
) : PassengerResultExpectedResponse(type)

sealed class RatingResultExpectedResponse(open val type: RatingResultExpectedResponseType)

enum class RatingResultExpectedResponseType {
    RATED,
    NOT_BEEN_RATED,
}

data class RatedExpectedResponse(
    override val type: RatingResultExpectedResponseType = RATED,
    val rating: Double,
) : RatingResultExpectedResponse(type)

data class NotBeenRatedExpectedResponse(
    override val type: RatingResultExpectedResponseType = NOT_BEEN_RATED,
) : RatingResultExpectedResponse(type)
