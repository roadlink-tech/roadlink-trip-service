package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.trip.section.Section
import java.time.Instant

/**
 * A trip plan is a possible combination of section which will be retrieved by the searcher.
 * So it's one of the possible trips elegible by a user.
 */
data class TripPlan(val sections: List<Section>) {
    operator fun plus(section: Section): TripPlan {
        return TripPlan(sections + section)
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