package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.IdGenerator
import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.trip.section.Section
import java.util.*

data class Trip(
    val id: String,
    // TODO Is it the driver id? if it's then why not use a UUID instead of string
    val driver: String,
    val vehicle: String,
    val departure: TripPoint,
    val arrival: TripPoint,
    val meetingPoints: List<TripPoint>,
    // TODO rename it by seats to be used. It'll be the vehicle capacity
    val availableSeats: Int,
) {

    // TODO revisar la creacion del trip y como lo itero
    fun sections(idGenerator: IdGenerator): Set<Section> {
        val allTripPoints = buildList {
            add(departure)
            addAll(meetingPoints)
            add(arrival)
        }
        val sections = mutableListOf<Section>()
        for (i in allTripPoints.indices) {
            if (isTheEndOfTheWay(allTripPoints, i)) {
                return sections.toSet()
            }
            val section = Section(
                id = idGenerator.id(),
                tripId = UUID.randomUUID(),
                departure = allTripPoints[i],
                arrival = allTripPoints[i + 1],
                distanceInMeters = 0.0,
                driver = driver,
                vehicle = vehicle,
                initialAmountOfSeats = availableSeats,
                bookedSeats = 0
            )
            sections.add(section)
        }
        return sections.toSet()
    }

    private fun isTheEndOfTheWay(
        allTripPoints: List<TripPoint>,
        i: Int
    ) = allTripPoints[i] == arrival

    fun isInTimeRange(timeRange: TimeRange): Boolean =
        TimeRange(departure.estimatedArrivalTime, arrival.estimatedArrivalTime).intersects(timeRange)
}
