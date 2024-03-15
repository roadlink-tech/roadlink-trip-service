package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.responses.DriverTripApplicationExpectedResponse
import com.roadlink.tripservice.infrastructure.rest.responses.PassengerExpectedResponse
import com.roadlink.tripservice.infrastructure.rest.responses.RatedExpectedResponse
import com.roadlink.tripservice.infrastructure.rest.responses.TripApplicationStatusExpectedResponse
import io.mockk.InternalPlatformDsl.toStr
import java.util.UUID

object DriverTripApplicationResponseFactory {
    fun avCabildoWithASingleTripApplicationPendingApproval(
        tripApplicationId: UUID,
        userId: UUID
    ): DriverTripApplicationExpectedResponse =
        DriverTripApplicationExpectedResponse(
            tripLegSolicitudeId = tripApplicationId.toString(),
            passenger = PassengerExpectedResponse(
                id = userId.toString(),
                fullName = "John Krasinski",
                rating = RatedExpectedResponse(rating = 1.3),
            ),
            status = TripApplicationStatusExpectedResponse.PENDING_APPROVAL,
            addressJoinStart = AddressResponseFactory.avCabildo_4853(),
            addressJoinEnd = AddressResponseFactory.avCabildo_20(),
        )
}