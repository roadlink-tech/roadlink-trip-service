package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.domain.time.TimeProvider
import com.roadlink.tripservice.domain.trip_search.TripPlan
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus.*
import com.roadlink.tripservice.domain.driver_trip.DriverSectionDetail
import com.roadlink.tripservice.domain.driver_trip.DriverTripDetail
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus
import com.roadlink.tripservice.domain.trip.TripStatus
import com.roadlink.tripservice.domain.trip.TripStatus.*
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import java.util.UUID

class GetDriverTripDetail(
    private val sectionRepository: SectionRepository,
    private val tripPlanApplicationRepository: TripPlanApplicationRepository,
    private val tripApplicationRepository: TripApplicationRepository,
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
                hasPendingApplications = hasPendingApplications(input.tripId),
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

    private fun hasPendingApplications(tripId: UUID): Boolean =
        tripApplicationRepository.findByTripId(tripId)
            .any { tripApplication -> tripApplication.isPending() }

    data class Input(val tripId: UUID)
}
