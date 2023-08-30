package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.domain.SectionFactory
import com.roadlink.tripservice.trip.domain.TripFactory
import com.roadlink.tripservice.trip.infrastructure.rest.responses.*

object DriverTripDetailResponseFactory {
    fun avCabildoWithASingleTripApplicationConfirmed(tripId: String = TripFactory.avCabildo_id ): DriverTripDetailExpectedResponse =
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
                            id = "PAINN",
                            fullName = "Painn Wilson",
                            rating = NotBeenRatedExpectedResponse(),
                        )
                    ),
                )
            ),
        )
}