package com.roadlink.tripservice.domain.trip_application

import java.util.*

interface TripPlanApplicationRepository {
    fun insert(tripPlanApplication: TripPlanApplication)
    fun update(tripPlanApplication: TripPlanApplication)
    fun find(commandQuery: CommandQuery): List<TripPlanApplication>
    data class CommandQuery(
        val ids: List<UUID> = emptyList(),
        val tripApplicationId: UUID? = null,
        val passengerId: UUID? = null,
    )
}