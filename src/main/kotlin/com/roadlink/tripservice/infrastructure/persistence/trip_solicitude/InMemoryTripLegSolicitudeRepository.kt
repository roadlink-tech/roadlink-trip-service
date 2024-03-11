package com.roadlink.tripservice.infrastructure.persistence.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import java.util.*

class InMemoryTripLegSolicitudeRepository(
    private val tripLegSolicitudes: MutableList<TripPlanSolicitude.TripLegSolicitude> = mutableListOf(),
) : TripLegSolicitudeRepository {
    override fun saveAll(tripLegSolicitudes: List<TripPlanSolicitude.TripLegSolicitude>) {
        tripLegSolicitudes.forEach { application ->
            this.tripLegSolicitudes.removeIf {
                it.id == application.id
            }
        }
        this.tripLegSolicitudes.addAll(tripLegSolicitudes)
    }

    private fun findAllByDriverId(driverId: UUID): List<TripPlanSolicitude.TripLegSolicitude> {
        return tripLegSolicitudes.filter { tripApplication -> tripApplication.driverId() == driverId }
    }

    private fun findByTripId(tripId: UUID): List<TripPlanSolicitude.TripLegSolicitude> {
        return tripLegSolicitudes.filter { it.tripId() == tripId }
    }

    private fun findBySectionId(sectionId: String): Set<TripPlanSolicitude.TripLegSolicitude> {
        return tripLegSolicitudes
            .filter { tripApplication ->
                tripApplication.sections.any { it.id == sectionId }
            }
            .toSet()
    }

    override fun find(commandQuery: TripLegSolicitudeRepository.CommandQuery): List<TripPlanSolicitude.TripLegSolicitude> {
        if (commandQuery.sectionId.isNotEmpty()) {
            return findBySectionId(commandQuery.sectionId).toMutableList()
        }
        if (commandQuery.tripId != null) {
            return findByTripId(commandQuery.tripId)
        }
        if (commandQuery.driverId != null) {
            return findAllByDriverId(commandQuery.driverId)
        }
        TODO()
    }
}