package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import java.util.*

class InMemoryTripApplicationRepository(
    private val tripApplications: MutableList<TripPlanApplication.TripApplication> = mutableListOf(),
) : TripApplicationRepository {
    override fun saveAll(tripApplications: List<TripPlanApplication.TripApplication>) {
        this.tripApplications.addAll(tripApplications)
    }

    override fun findAllByDriverId(driverId: UUID): List<TripPlanApplication.TripApplication> {
        return tripApplications.filter { tripApplication -> tripApplication.driverId() == driverId }
    }

    override fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication> {
        return tripApplications.filter { it.tripId() == tripId }
    }
}