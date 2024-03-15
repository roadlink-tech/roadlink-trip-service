package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.*
import com.roadlink.tripservice.infrastructure.rest.responses.*
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import java.util.*

object DriverTripDetailResponseFactory {
    fun avCabildoWithASingleTripApplicationConfirmed(
        tripId: String = TripFactory.avCabildo_id,
        userId: UUID
    ): DriverTripDetailResponse =
        DriverTripDetailResponse(
            tripId = UUID.fromString(tripId),
            tripStatus = DriverTripStatusResponse.FINISHED,
            seatStatus = SeatsAvailabilityStatusResponse.SOME_SEATS_AVAILABLE,
            hasPendingApplications = false,
            sectionDetails = listOf(
                DriverSectionDetailResponse(
                    sectionId = SectionFactory.avCabildo_id,
                    departure = TripPointResponseFactory.avCabildo_4853(),
                    arrival = TripPointResponseFactory.avCabildo_20(),
                    occupiedSeats = 1,
                    availableSeats = 3,
                    passengers = listOf(
                        PassengerResultResponse.PassengerResponse(
                            type = PassengerResultResponseType.PASSENGER,
                            id = userId.toString(),
                            fullName = "Painn Wilson",
                            rating = ScoreResultResponse.NotBeenScoredResponse(),
                        )
                    ),
                )
            ),
        )
}