package com.roadlink.tripservice.domain.trip_plan

import java.util.*

/**
 * when all the trip leg solicitudes are confirmed, then the trip plan solicitude is confirmed and a trip plan can be created
 */
interface TripPlanRepository {
    fun insert(tripPlan: TripPlan): TripPlan
    fun update(tripPlan: TripPlan): TripPlan
    fun find(commandQuery: CommandQuery): List<TripPlan>
    data class CommandQuery(
        val id: UUID? = null,
        val passengerId: UUID? = null,
        val tripId: UUID? = null
    )
}