package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.domain.driver_trip.DriverTripApplication
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import java.util.UUID

class GetDriverTripApplications(
    private val tripApplicationRepository: TripApplicationRepository,
    private val userRepository: UserRepository,
    private val ratingRepository: RatingRepository,
) {
    operator fun invoke(input: Input): List<DriverTripApplication> {
        return tripApplicationRepository.findByTripId(input.tripId)
            .filter { tripApplication ->
                tripApplication.isPending()
            }
            .map { tripApplication ->
                val passengerId = tripApplication.passengerId
                DriverTripApplication(
                    tripApplicationId = tripApplication.id,
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
                    applicationStatus = tripApplication.status,
                    addressJoinStart = tripApplication.departureTripPoint().address,
                    addressJoinEnd = tripApplication.arrivalTripPoint().address,
                )
            }
    }

    data class Input(val tripId: UUID)
}