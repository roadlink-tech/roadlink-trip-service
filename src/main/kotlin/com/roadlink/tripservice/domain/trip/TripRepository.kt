package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import java.util.*

interface TripRepository {
    fun save(trip: Trip)
    fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean
    fun findAllByDriverId(driverId: UUID): List<Trip>
    fun find(commandQuery: CommandQuery): List<Trip>
    data class CommandQuery(
        val ids: List<UUID> = emptyList(),
        val driverId: UUID? = null,
    )
}
