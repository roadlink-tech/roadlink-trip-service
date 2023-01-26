package com.roadlink.tripservice.trip.infrastructure.persistence

import com.roadlink.tripservice.trip.domain.TimeRange
import com.roadlink.tripservice.trip.domain.Trip
import com.roadlink.tripservice.trip.domain.TripRepository

class InMemoryTripRepository(
    private val trips: MutableList<Trip> = mutableListOf(),
) : TripRepository {

    override fun save(trip: Trip) {
        trips.add(trip)
    }

    override fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean {
        return trips.any { it.driver == driver && it.isInTimeRange(timeRange) }
    }

    fun findAll(): List<Trip> = trips

    fun isEmpty(): Boolean = trips.isEmpty()
}