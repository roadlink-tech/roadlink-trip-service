package com.roadlink.tripservice.infrastructure.persistence.trip

import com.roadlink.tripservice.domain.common.utils.time.TimeRange
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
        return trips.any { it.driverId == driver && it.isInTimeRange(timeRange) }
    }

    override fun findAllByDriverId(driverId: UUID): List<Trip> {
        return trips.filter { it.driverId == driverId.toString() }
    }

    override fun find(commandQuery: TripRepository.CommandQuery): List<Trip> {
        TODO("Not yet implemented")
    }

    fun findAll(): List<Trip> = trips

    fun isEmpty(): Boolean = trips.isEmpty()

    fun deleteAll() {
        trips.clear()
    }
}