package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import java.util.*

class InMemoryTripPlanApplicationRepository(
    private val tripPlanApplications: MutableList<TripPlanApplication> = mutableListOf(),
) : TripPlanApplicationRepository {
    override fun save(application: TripPlanApplication) {
        tripPlanApplications.removeIf {
            it.id == application.id
        }
        tripPlanApplications.add(application)
    }

    override fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication? {
        return tripPlanApplications
            .firstOrNull { it.tripApplications.any { tripApplication -> tripApplication.id == tripApplicationId } }
    }
}