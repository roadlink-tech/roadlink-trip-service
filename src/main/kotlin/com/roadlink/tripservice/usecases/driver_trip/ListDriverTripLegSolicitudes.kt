package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.domain.driver_trip.DriverTripLegSolicitude
import com.roadlink.tripservice.domain.driver_trip.Passenger
import com.roadlink.tripservice.domain.driver_trip.PassengerNotExists
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserRepository
import java.util.UUID

class ListDriverTripLegSolicitudes(
    private val tripLegSolicitudeRepository: TripLegSolicitudeRepository,
    private val userRepository: UserRepository,
    private val ratingRepository: RatingRepository,
) {
    operator fun invoke(input: Input): List<DriverTripLegSolicitude> {
        return tripLegSolicitudeRepository.find(TripLegSolicitudeRepository.CommandQuery(tripId = input.tripId))
            .filter { tripLegSolicitude ->
                tripLegSolicitude.isPendingApproval()
            }
            .map { tripLegSolicitude ->
                val passengerId = tripLegSolicitude.passengerId
                DriverTripLegSolicitude(
                    tripLegSolicitudeId = tripLegSolicitude.id,
                    passenger = userRepository.findFullNameById(passengerId)
                        ?.let { fullName ->
                            Passenger(
                                id = passengerId,
                                fullName = fullName,
                                rating = ratingRepository.findByUserId(passengerId)
                                    ?.let { rating ->
                                        Rated(rating)
                                    }
                                    ?: NotBeenRated,
                            )
                        }
                        ?: PassengerNotExists(id = passengerId),
                    status = tripLegSolicitude.status,
                    addressJoinStart = tripLegSolicitude.departureTripPoint().address,
                    addressJoinEnd = tripLegSolicitude.arrivalTripPoint().address,
                )
            }
    }

    data class Input(val tripId: UUID)
}