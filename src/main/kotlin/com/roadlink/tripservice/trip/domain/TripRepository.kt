package com.roadlink.tripservice.trip.domain

interface TripRepository {
    fun save(trip: Trip)
    fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean
}
