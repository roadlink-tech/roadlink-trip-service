package com.roadlink.tripservice.usecases.trip_summary

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_summary.driver.DriverTripSummary
import com.roadlink.tripservice.usecases.UseCase
import java.util.*

// TODO test it!!
class RetrieveDriverTripSummary(
    private val tripRepository: TripRepository,
    private val sectionsRepository: SectionRepository
) : UseCase<UUID, RetrieveDriverTripSummaryOutput> {
    override fun invoke(input: UUID): RetrieveDriverTripSummaryOutput {
        val trips = tripRepository.findAllByDriverId(input)
        val sectionsByTripId = groupSectionsByTripId(trips)
        val canReceiveAnyPassengerByTripId: Map<UUID, Boolean> =
            sectionsByTripId.map { entry -> Pair(entry.key, entry.value.any { it.canReceiveAnyPassenger() }) }.toMap()
        return createOutput(trips, canReceiveAnyPassengerByTripId)
    }

    private fun createOutput(
        trips: List<Trip>,
        canReceiveAnyPassengerByTripId: Map<UUID, Boolean>
    ): RetrieveDriverTripSummaryOutput {
        return RetrieveDriverTripSummaryOutput(trips.map {
            DriverTripSummary(
                trip = it,
                hasAvailableSeats = canReceiveAnyPassengerByTripId[UUID.fromString(it.id)]!!
            )
        })
    }

    private fun groupSectionsByTripId(trips: List<Trip>): Map<UUID, List<Section>> {
        return sectionsRepository.findAllByTripIds(trips.map { UUID.fromString(it.id) }).groupBy { it.tripId }
    }
}

class RetrieveDriverTripSummaryOutput(val trips: List<DriverTripSummary>)