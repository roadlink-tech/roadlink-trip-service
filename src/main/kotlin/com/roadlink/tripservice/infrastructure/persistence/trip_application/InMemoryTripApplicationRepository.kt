package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import java.util.*

class InMemoryTripApplicationRepository(
    private val tripApplications: MutableList<TripPlanApplication.TripApplication> = mutableListOf(),
) : TripApplicationRepository {
    override fun saveAll(tripApplications: List<TripPlanApplication.TripApplication>) {
        tripApplications.forEach { application ->
            this.tripApplications.removeIf {
                it.id == application.id
            }
        }
        this.tripApplications.addAll(tripApplications)
    }

    private fun findAllByDriverId(driverId: UUID): List<TripPlanApplication.TripApplication> {
        return tripApplications.filter { tripApplication -> tripApplication.driverId() == driverId }
    }

    private fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication> {
        return tripApplications.filter { it.tripId() == tripId }
    }

    private fun findBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication> {
        return tripApplications
            .filter { tripApplication ->
                tripApplication.sections.any { it.id == sectionId }
            }
            .toSet()
    }

    override fun find(commandQuery: TripApplicationRepository.CommandQuery): List<TripPlanApplication.TripApplication> {
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