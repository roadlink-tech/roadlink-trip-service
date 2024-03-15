package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripLegSolicitudeResponse
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.PassengerResultResponse
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.ScoreResultResponse
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripLegSolicitudeStatusResponse
import java.util.*

object DriverTripApplicationResponseFactory {
    fun avCabildoWithASingleTripApplicationPendingApproval(
        tripApplicationId: UUID,
        userId: UUID
    ): DriverTripLegSolicitudeResponse =
        DriverTripLegSolicitudeResponse(
            tripLegSolicitudeId = tripApplicationId.toString(),
            passenger = PassengerResultResponse.PassengerResponse(
                id = userId.toString(),
                fullName = "John Krasinski",
                rating = ScoreResultResponse.ScoreResponse(score = 1.3),
            ),
            status = TripLegSolicitudeStatusResponse.PENDING_APPROVAL,
            addressJoinStart = AddressResponseFactory.avCabildo_4853(),
            addressJoinEnd = AddressResponseFactory.avCabildo_20(),
        )
}
