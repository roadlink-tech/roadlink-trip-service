package com.roadlink.tripservice.usecases.driver_trip

import com.roadlink.tripservice.domain.driver_trip.DriverSectionDetail
import com.roadlink.tripservice.domain.driver_trip.DriverTripDetail
import com.roadlink.tripservice.domain.driver_trip.PassengerNotExists
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus
import com.roadlink.tripservice.domain.driver_trip.SeatsAvailabilityStatus.*
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.ACCEPTED
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.TripLegSolicitude.Status.PENDING_APPROVAL
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.domain.user.UserTrustScoreRepository
import java.util.*

class RetrieveDriverTripDetail(
    private val sectionRepository: SectionRepository,
    private val tripRepository: TripRepository,
    private val tripLegSolicitudeRepository: TripLegSolicitudeRepository,
    private val userRepository: UserRepository,
    private val userTrustScoreRepository: UserTrustScoreRepository,
) {
    operator fun invoke(input: Input): DriverTripDetail {
        val trip = tripRepository.find(TripRepository.CommandQuery(ids = listOf(input.tripId))).first()
        val sections = sectionRepository.findAllByTripIdOrFail(input.tripId)
        // TODO validar con martin por que estamos usando TripSearchPlanResult (anterior TripPlan)
        return TripSearchPlanResult(sections).let { tripPlan ->
            DriverTripDetail(
                tripId = input.tripId,
                tripStatus = trip.status,
                seatStatus = seatsAvailabilityStatusOf(tripPlan),
                hasPendingApplications = hasPendingTripLegSolicitudes(input.tripId),
                sectionDetails = tripPlan.sections.map { section ->
                    DriverSectionDetail(
                        sectionId = section.id,
                        departure = section.departure,
                        arrival = section.arrival,
                        occupiedSeats = section.occupiedSeats(),
                        availableSeats = section.availableSeats(),
                        passengers = tripLegSolicitudeRepository.find(
                            TripLegSolicitudeRepository.CommandQuery(sectionId = section.id, status = ACCEPTED)
                        ).map { it.passengerId }.map { passengerId ->
                            userRepository.findByUserId(passengerId)?.let {
                                val userTrustScore = userTrustScoreRepository.findById(passengerId)
                                it.toPassenger(userTrustScore)
                            } ?: kotlin.run {
                                PassengerNotExists(passengerId)
                            }
                        },
                    )
                },
            )
        }
    }

    private fun seatsAvailabilityStatusOf(tripSearchPlanResult: TripSearchPlanResult): SeatsAvailabilityStatus =
        when {
            tripSearchPlanResult.hasNoSeatsAvailable() -> NO_SEATS_AVAILABLE
            tripSearchPlanResult.hasAllSeatsAvailable() -> ALL_SEATS_AVAILABLE
            else -> SOME_SEATS_AVAILABLE
        }

    private fun hasPendingTripLegSolicitudes(tripId: UUID): Boolean =
        tripLegSolicitudeRepository.find(
            TripLegSolicitudeRepository.CommandQuery(
                tripId = tripId, status = PENDING_APPROVAL
            )
        ).isNotEmpty()

    data class Input(val tripId: UUID)
}
