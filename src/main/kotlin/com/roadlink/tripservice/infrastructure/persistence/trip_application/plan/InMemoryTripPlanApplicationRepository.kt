package com.roadlink.tripservice.infrastructure.persistence.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import java.util.*

class InMemoryTripPlanApplicationRepository(
    private val tripPlanApplications: MutableList<TripPlanApplication> = mutableListOf(),
    private val tripApplicationRepository: TripApplicationRepository
) : TripPlanApplicationRepository {
    override fun insert(tripPlanApplication: TripPlanApplication) {
        tripPlanApplications.removeIf {
            it.id == tripPlanApplication.id
        }
        tripPlanApplications.add(tripPlanApplication)
        tripApplicationRepository.saveAll(tripPlanApplication.tripApplications)
    }

    override fun update(tripPlanApplication: TripPlanApplication) {
        tripPlanApplications.removeIf {
            it.id == tripPlanApplication.id
        }
        tripPlanApplications.add(tripPlanApplication)
        tripApplicationRepository.saveAll(tripPlanApplication.tripApplications)
    }

    override fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication? {
        return tripPlanApplications
            .firstOrNull { it.tripApplications.any { tripApplication -> tripApplication.id == tripApplicationId } }
    }

    override fun findById(id: UUID): TripPlanApplication? {
        return tripPlanApplications.firstOrNull { it.id == id }
    }

    override fun find(commandQuery: TripPlanApplicationRepository.CommandQuery): List<TripPlanApplication> {
        if (commandQuery.ids.isNotEmpty()) {
            val result = findById(commandQuery.ids.first())
            return if (result != null) {
                return listOf(result)
            } else emptyList()
        }
        if (commandQuery.passengerId != null) {
            return emptyList()
        }
        if (commandQuery.tripApplicationId != null) {
            val result = findByTripApplicationId(commandQuery.tripApplicationId)
            return if (result != null) {
                return listOf(result)
            } else emptyList()
        }
        TODO()
    }

    fun deleteAll() {
        this.tripPlanApplications.clear()
    }
}