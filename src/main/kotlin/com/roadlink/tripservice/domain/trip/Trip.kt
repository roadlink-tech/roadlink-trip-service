package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.trip.section.Section

data class Trip(
    val id: String,
    val driver: String,
    val vehicle: String,
    val departure: TripPoint,
    val arrival: TripPoint,
    val meetingPoints: List<TripPoint>,
    val availableSeats: Int,
) {

    // TODO revisar la creacion del trip y como lo itero
    fun sections(): Set<Section> {
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
                departure = allTripPoints[i],
                arrival = allTripPoints[i + 1],
                distanceInMeters = 0.0,
                driver = "",
                vehicle = "",
                availableSeats = 2
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
        TimeRange(departure.at, arrival.at).intersects(timeRange)
}
