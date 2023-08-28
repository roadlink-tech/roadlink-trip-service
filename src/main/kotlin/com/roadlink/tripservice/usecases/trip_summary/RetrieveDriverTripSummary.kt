package com.roadlink.tripservice.usecases.trip_summary

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_summary.driver.DriverTripSummary
import com.roadlink.tripservice.usecases.UseCase
import java.util.*


class RetrieveDriverTripSummary(
    private val tripRepository: TripRepository,
    private val sectionsRepository: SectionRepository,
    private val tripApplicationRepository: TripApplicationRepository
) : UseCase<String, RetrieveDriverTripSummaryOutput> {

    // TODO change the input value by UUID, when driver type into Trip entity was update
    override fun invoke(input: String): RetrieveDriverTripSummaryOutput {
        val driverId = UUID.fromString(input)
        val trips = tripRepository.findAllByDriverId(driverId).ifEmpty {
            return RetrieveDriverTripSummaryOutput.DriverTripSummariesNotFound(driverId)
        }
        val canReceiveAnyPassengerByTripId = mapTripsThatCanReceivePassengers(trips)
        val hasPendingApplicationsByTripId = mapTripsThatHasPendingApplications(driverId)
        return createOutput(trips, canReceiveAnyPassengerByTripId, hasPendingApplicationsByTripId)
    }

    private fun mapTripsThatHasPendingApplications(driverId: UUID): Map<UUID, Boolean> {
        val tripApplications = tripApplicationRepository.findAllByDriverId(driverId)
        val tipApplicationsGroupByTripId = groupTripApplicationsByTripId(tripApplications)
        val hasPendingApplicationsByTripId: Map<UUID, Boolean> = tipApplicationsGroupByTripId.map { entry ->
            Pair(entry.key, entry.value.any { it.isPending() })
        }.toMap()
        return hasPendingApplicationsByTripId
    }

    private fun groupTripApplicationsByTripId(tripApplications: List<TripPlanApplication.TripApplication>)
            : Map<UUID, List<TripPlanApplication.TripApplication>> {
        return tripApplications.groupBy { it.tripId() }
    }

    private fun mapTripsThatCanReceivePassengers(trips: List<Trip>): Map<UUID, Boolean> {
        val sectionsByTripId = groupSectionsByTripId(trips)
        return sectionsByTripId.map { entry -> Pair(entry.key, entry.value.any { it.canReceiveAnyPassenger() }) }
            .toMap()
    }

    private fun createOutput(
        trips: List<Trip>,
        canReceiveAnyPassengerByTripId: Map<UUID, Boolean>,
        hasPendingApplicationsByTripId: Map<UUID, Boolean>
    ): RetrieveDriverTripSummaryOutput {
        return RetrieveDriverTripSummaryOutput.DriverTripSummariesFound(trips.map {
            DriverTripSummary(
                trip = it,
                hasAvailableSeats = canReceiveAnyPassengerByTripId[UUID.fromString(it.id)]!!,
                hasPendingApplications = hasPendingApplicationsByTripId[UUID.fromString(it.id)] ?: false
            )
        })
    }

    private fun groupSectionsByTripId(trips: List<Trip>): Map<UUID, List<Section>> {
        return sectionsRepository.findAllByTripIds(trips.map { UUID.fromString(it.id) }).groupBy { it.tripId }
    }
}

sealed class RetrieveDriverTripSummaryOutput {
    data class DriverTripSummariesFound(val trips: List<DriverTripSummary>) : RetrieveDriverTripSummaryOutput()
    data class DriverTripSummariesNotFound(val driverId: UUID) : RetrieveDriverTripSummaryOutput()
}