package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.domain.driver_trip.DriverTripLegSolicitude
import com.roadlink.tripservice.domain.driver_trip.PassengerNotExists
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.*
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.*
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import java.util.UUID

class ListDriverTripLegSolicitudes(
    private val tripLegSolicitudeRepository: TripLegSolicitudeRepository,
    private val userRepository: UserRepository,
    private val userTrustScoreRepository: UserTrustScoreRepository,
) {
    operator fun invoke(input: Input): List<DriverTripLegSolicitude> {
        return tripLegSolicitudeRepository.find(
            TripLegSolicitudeRepository.CommandQuery(
                tripId = input.tripId,
                status = PENDING_APPROVAL
            )
        ).map { tripLegSolicitude ->
            val passengerId = tripLegSolicitude.passengerId
            DriverTripLegSolicitude(
                tripLegSolicitudeId = tripLegSolicitude.id,
                passenger = userRepository.findByUserId(passengerId)
                    ?.let { user ->
                        val userTrustScore = userTrustScoreRepository.findById(passengerId)
                        user.asPassengerWith(userTrustScore)
                    } ?: PassengerNotExists(id = passengerId),
                status = tripLegSolicitude.status,
                addressJoinStart = tripLegSolicitude.departureTripPoint().address,
                addressJoinEnd = tripLegSolicitude.arrivalTripPoint().address,
            )
        }
    }

    data class Input(val tripId: UUID)
}