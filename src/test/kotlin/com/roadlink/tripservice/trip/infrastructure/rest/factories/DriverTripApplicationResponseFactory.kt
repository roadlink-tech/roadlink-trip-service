package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.infrastructure.rest.responses.*
import java.util.UUID

object DriverTripApplicationResponseFactory {
    fun avCabildoWithASingleTripApplicationPendingApproval(tripApplicationId: UUID): DriverTripApplicationExpectedResponse =
        DriverTripApplicationExpectedResponse(
            tripApplicationId = tripApplicationId.toString(),
            passenger = PassengerExpectedResponse(
                id = "JOHN",
                fullName = "John Krasinski",
                rating = RatedExpectedResponse(rating = 1.3),
            ),
            applicationStatus = TripApplicationStatusExpectedResponse.PENDING_APPROVAL,
            addressJoinStart = AddressResponseFactory.avCabildo_4853(),
            addressJoinEnd = AddressResponseFactory.avCabildo_20(),
        )
}