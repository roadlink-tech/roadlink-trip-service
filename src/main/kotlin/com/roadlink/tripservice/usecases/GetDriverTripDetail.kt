package com.roadlink.tripservice.usecases

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.domain.time.TimeProvider
import com.roadlink.tripservice.domain.trip.TripPlan
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.domain.SeatsAvailabilityStatus.*
import com.roadlink.tripservice.domain.TripStatus.*

class GetDriverTripDetail(
    private val sectionRepository: SectionRepository,
    private val tripPlanApplicationRepository: TripPlanApplicationRepository,
    private val userRepository: UserRepository,
    private val ratingRepository: RatingRepository,
    private val timeProvider: TimeProvider,
) {
    operator fun invoke(input: Input): DriverTripDetail =
        sectionRepository.findByTripId(input.tripId).let { tripPlan ->
            DriverTripDetail(
                tripId = input.tripId,
                tripStatus = tripStatusOf(tripPlan),
                seatStatus = seatsAvailabilityStatusOf(tripPlan),
                sectionDetails = tripPlan.sections
                    .map { section ->
                        DriverSectionDetail(
                            sectionId = section.id,
                            departure = section.departure,
                            arrival = section.arrival,
                            occupiedSeats = section.occupiedSeats(),
                            availableSeats = section.availableSeats(),
                            passengers = tripPlanApplicationRepository.findTripApplicationBySectionId(section.id)
                                .filter { it.isConfirmed() }
                                .map { it.passengerId }
                                .map { passengerId ->
                                    userRepository.findFullNameById(passengerId)
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
                                        ?: PassengerNotExists(id = passengerId)
                                },
                        )
                    },
            )
        }

    private fun tripStatusOf(tripPlan: TripPlan): TripStatus =
        when {
            tripPlan.departureAt().isAfter(timeProvider.now()) -> NOT_STARTED
            tripPlan.arriveAt().isBefore(timeProvider.now()) -> FINISHED
            else -> IN_PROGRESS
        }

    private fun seatsAvailabilityStatusOf(tripPlan: TripPlan): SeatsAvailabilityStatus =
        when {
            tripPlan.hasNoSeatsAvailable() -> NO_SEATS_AVAILABLE
            tripPlan.hasAllSeatsAvailable() -> ALL_SEATS_AVAILABLE
            else -> SOME_SEATS_AVAILABLE
        }

    data class Input(val tripId: String)
}