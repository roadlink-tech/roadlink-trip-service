package com.roadlink.tripservice.infrastructure.persistence.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import java.util.*

class InMemoryTripPlanSolicitudeRepository(
    private val tripPlanSolicitudes: MutableList<TripPlanSolicitude> = mutableListOf(),
    private val tripLegSolicitudeRepository: TripLegSolicitudeRepository
) : TripPlanSolicitudeRepository {
    override fun insert(tripPlanSolicitude: TripPlanSolicitude) {
        tripPlanSolicitudes.removeIf {
            it.id == tripPlanSolicitude.id
        }
        tripPlanSolicitudes.add(tripPlanSolicitude)
        tripLegSolicitudeRepository.saveAll(tripPlanSolicitude.tripLegSolicitudes)
    }

    override fun update(tripPlanSolicitude: TripPlanSolicitude) {
        tripPlanSolicitudes.removeIf {
            it.id == tripPlanSolicitude.id
        }
        tripPlanSolicitudes.add(tripPlanSolicitude)
        tripLegSolicitudeRepository.saveAll(tripPlanSolicitude.tripLegSolicitudes)
    }

    private fun findByTripApplicationId(tripApplicationId: UUID): TripPlanSolicitude? {
        return tripPlanSolicitudes
            .firstOrNull { it.tripLegSolicitudes.any { tripApplication -> tripApplication.id == tripApplicationId } }
    }

    private fun findById(id: UUID): TripPlanSolicitude? {
        return tripPlanSolicitudes.firstOrNull { it.id == id }
    }

    override fun find(commandQuery: TripPlanSolicitudeRepository.CommandQuery): List<TripPlanSolicitude> {
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
        this.tripPlanSolicitudes.clear()
    }
}