package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.domain.*
import com.roadlink.tripservice.domain.driver_trip.*
import com.roadlink.tripservice.domain.common.utils.time.TimeProvider
import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus.*
import com.roadlink.tripservice.domain.trip.TripStatus
import com.roadlink.tripservice.domain.trip.TripStatus.*
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.user.UserRepository
import java.util.UUID

class RetrieveDriverTripDetail(
    private val sectionRepository: SectionRepository,
    private val tripLegSolicitudeRepository: TripLegSolicitudeRepository,
    private val userRepository: UserRepository,
    private val ratingRepository: RatingRepository,
    private val timeProvider: TimeProvider,
) {
    operator fun invoke(input: Input): DriverTripDetail {
        val sections = sectionRepository.findAllByTripIdOrFail(input.tripId)
        return TripSearchPlanResult(sections).let { tripPlan ->
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
                            passengers = tripLegSolicitudeRepository.find(TripLegSolicitudeRepository.CommandQuery(sectionId = section.id))
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
    }

    private fun tripStatusOf(tripSearchPlanResult: TripSearchPlanResult): TripStatus =
        when {
            tripSearchPlanResult.departureAt().isAfter(timeProvider.now()) -> NOT_STARTED
            tripSearchPlanResult.arriveAt().isBefore(timeProvider.now()) -> FINISHED
            else -> IN_PROGRESS
        }

    private fun seatsAvailabilityStatusOf(tripSearchPlanResult: TripSearchPlanResult): SeatsAvailabilityStatus =
        when {
            tripSearchPlanResult.hasNoSeatsAvailable() -> NO_SEATS_AVAILABLE
            tripSearchPlanResult.hasAllSeatsAvailable() -> ALL_SEATS_AVAILABLE
            else -> SOME_SEATS_AVAILABLE
        }

    private fun hasPendingApplications(tripId: UUID): Boolean =
        tripLegSolicitudeRepository.find(TripLegSolicitudeRepository.CommandQuery(tripId = tripId))
            .any { tripApplication -> tripApplication.isPendingApproval() }

    data class Input(val tripId: UUID)
}
