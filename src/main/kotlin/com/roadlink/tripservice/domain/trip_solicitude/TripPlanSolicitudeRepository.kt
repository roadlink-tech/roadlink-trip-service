package com.roadlink.tripservice.domain.trip_solicitude

import java.util.*

interface TripPlanSolicitudeRepository {
    fun insert(tripPlanSolicitude: TripPlanSolicitude)
    fun update(tripPlanSolicitude: TripPlanSolicitude)
    fun find(commandQuery: CommandQuery): List<TripPlanSolicitude>
    data class CommandQuery(
        val ids: List<UUID> = emptyList(),
        val tripApplicationId: UUID? = null,
        val passengerId: UUID? = null,
    )
}