package com.roadlink.tripservice.infrastructure.persistence.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import java.util.*

class InMemoryTripPlanApplicationRepository(
    private val tripPlanApplications: MutableList<TripPlanApplication> = mutableListOf(),
    private val tripApplicationRepository: TripApplicationRepository
) : TripPlanApplicationRepository {
    override fun insert(application: TripPlanApplication) {
        tripPlanApplications.removeIf {
            it.id == application.id
        }
        tripPlanApplications.add(application)
        tripApplicationRepository.saveAll(application.tripApplications)
    }

    override fun update(application: TripPlanApplication) {
        tripPlanApplications.removeIf {
            it.id == application.id
        }
        tripPlanApplications.add(application)
        tripApplicationRepository.saveAll(application.tripApplications)
    }

    override fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication? {
        return tripPlanApplications
            .firstOrNull { it.tripApplications.any { tripApplication -> tripApplication.id == tripApplicationId } }
    }

    override fun findBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication> {
        return tripPlanApplications
            .flatMap { it.tripApplications }
            .filter { tripApplication ->
                tripApplication.sections.any { it.id == sectionId }
            }
            .toSet()
    }

    fun deleteAll() {
        this.tripPlanApplications.clear()
    }
}