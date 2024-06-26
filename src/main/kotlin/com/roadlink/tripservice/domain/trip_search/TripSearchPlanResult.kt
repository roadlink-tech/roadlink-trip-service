package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.section.Section
import java.time.Instant

/**
 * A trip plan search result is a possible combination of section which will be retrieved by the searcher.
 * So it's one of the possible eligible trips by a user.
 * */
data class TripSearchPlanResult(val sections: List<Section>) {
    operator fun plus(section: Section): TripSearchPlanResult {
        return TripSearchPlanResult(sections + section)
    }

    /**
     * listTrips will return all the different associated trips in the plan result
     * */
    fun listTrips(tripRepository: TripRepository): List<Trip> {
        return tripRepository.find(
            commandQuery = TripRepository.CommandQuery(
                ids = sections.map { it.tripId }.distinct().toList()
            )
        )
    }

    fun last(): Section = sections.last()

    fun distance(): Double = sections.sumOf { it.distance() }

    fun departureAt(): Instant =
        sections.first().departure.estimatedArrivalTime

    fun arriveAt(): Instant =
        sections.last().arrival.estimatedArrivalTime

    fun hasNoSeatsAvailable(): Boolean =
        sections.all { section -> section.hasNoSeatsAvailable() }

    fun hasAllSeatsAvailable(): Boolean =
        sections.all { section -> section.hasAllSeatsAvailable() }
}