package com.roadlink.tripservice.domain

data class Trip(
    val id: String,
    val driver: String,
    val vehicle: String,
    val departure: TripPoint,
    val arrival: TripPoint,
    val meetingPoints: List<TripPoint>,
    val availableSeats: Int,
) {
    fun isInTimeRange(timeRange: TimeRange): Boolean =
        TimeRange(departure.at, arrival.at).intersects(timeRange)
}
