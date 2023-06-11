package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import java.util.*

class InMemoryTripRepository(
    private val trips: MutableList<Trip> = mutableListOf(),
) : TripRepository {

    override fun save(trip: Trip) {
        trips.add(trip)
    }

    override fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean {
        return trips.any { it.driver == driver && it.isInTimeRange(timeRange) }
    }

    override fun findAllByDriverId(driverId: UUID): List<Trip> {
        return trips.filter { it.driver == driverId.toString() }
    }

    fun findAll(): List<Trip> = trips

    fun isEmpty(): Boolean = trips.isEmpty()

    fun deleteAll() {
        trips.clear()
    }
}