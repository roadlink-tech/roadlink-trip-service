package com.roadlink.tripservice.domain.trip_solicitude

import java.util.*

interface TripLegSolicitudeRepository {
    fun saveAll(tripLegSolicitudes: List<TripPlanSolicitude.TripLegSolicitude>)
    fun find(commandQuery: CommandQuery): List<TripPlanSolicitude.TripLegSolicitude>
    data class CommandQuery(
        val sectionId: String = "",
        val tripId: UUID? = null,
        val driverId: UUID? = null
    )
}
