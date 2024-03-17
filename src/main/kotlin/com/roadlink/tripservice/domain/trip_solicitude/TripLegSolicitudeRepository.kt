package com.roadlink.tripservice.domain.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude.*
import java.util.*

interface TripLegSolicitudeRepository {
    fun saveAll(tripLegSolicitudes: List<TripLegSolicitude>)
    fun find(commandQuery: CommandQuery): List<TripLegSolicitude>
    data class CommandQuery(
        val sectionId: String = "",
        val tripId: UUID? = null,
        val driverId: UUID? = null,
        val status: TripLegSolicitude.Status? = null
    )
}
