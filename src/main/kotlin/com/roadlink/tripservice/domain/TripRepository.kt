package com.roadlink.tripservice.domain

interface TripRepository {
    fun save(trip: Trip)
    fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean
}
