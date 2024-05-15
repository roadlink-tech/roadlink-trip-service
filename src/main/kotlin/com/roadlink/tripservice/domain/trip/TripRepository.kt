package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import java.util.*

interface TripRepository {
    fun insert(trip: Trip)
    fun update(trip: Trip)
    fun existsByDriverAndInTimeRange(driverId: String, timeRange: TimeRange): Boolean
    fun find(commandQuery: CommandQuery): List<Trip>
    data class CommandQuery(
        val ids: List<UUID> = emptyList(),
        val driverId: UUID? = null,
    )
}
