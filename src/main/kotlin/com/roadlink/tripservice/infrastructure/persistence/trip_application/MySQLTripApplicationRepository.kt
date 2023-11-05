package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import jakarta.persistence.EntityManager
import java.util.*

class MySQLTripApplicationRepository(
    private val entityManager: EntityManager,
) : TripApplicationRepository {
    override fun saveAll(tripApplications: List<TripPlanApplication.TripApplication>) {
        for (tripApplication in tripApplications) {
            entityManager.persist(TripApplicationJPAEntity.from(tripApplication))
        }
    }

    override fun findAllByDriverId(driverId: UUID): List<TripPlanApplication.TripApplication> {
        TODO("Not yet implemented")
    }

    override fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication> {
        TODO("Not yet implemented")
    }

}