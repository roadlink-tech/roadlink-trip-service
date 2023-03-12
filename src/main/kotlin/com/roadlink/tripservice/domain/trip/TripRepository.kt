package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.time.TimeRange

interface TripRepository {
    fun save(trip: Trip)
    fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean
}
