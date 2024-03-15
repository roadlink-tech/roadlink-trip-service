package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.responses.*
import com.roadlink.tripservice.usecases.factory.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import java.util.UUID

object DriverTripDetailResponseFactory {
    fun avCabildoWithASingleTripApplicationConfirmed(
        tripId: String = TripFactory.avCabildo_id,
        userId: UUID
    ): DriverTripDetailExpectedResponse =
        DriverTripDetailExpectedResponse(
            tripId = tripId,
            tripStatus = TripStatusExpectedResponse.FINISHED,
            seatStatus = SeatsAvailabilityStatusExpectedResponse.SOME_SEATS_AVAILABLE,
            hasPendingApplications = false,
            sectionDetails = listOf(
                DriverSectionDetailExpectedResponse(
                    sectionId = SectionFactory.avCabildo_id,
                    departure = TripPointResponseFactory.avCabildo_4853(),
                    arrival = TripPointResponseFactory.avCabildo_20(),
                    occupiedSeats = 1,
                    availableSeats = 3,
                    passengers = listOf(
                        PassengerExpectedResponse(
                            type = PassengerResultExpectedResponseType.PASSENGER,
                            id = userId.toString(),//"PAINN",
                            fullName = "Painn Wilson",
                            rating = NotBeenRatedExpectedResponse(),
                        )
                    ),
                )
            ),
        )
}