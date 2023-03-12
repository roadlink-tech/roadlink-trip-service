package com.roadlink.tripservice.domain.trip.section

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.TripPoint
import java.time.Instant

data class Section(
    val departure: TripPoint,
    val arrival: TripPoint,
    val distanceInMeters: Double,
    val driver: String,
    val vehicle: String,
    val availableSeats: Int,
) {

    fun departure(): Location = departure.location

    fun arrival(): Location = arrival.location

    fun arrivesTo(location: Location): Boolean {
        return arrival.location == location
    }

    fun departuresFrom(location: Location): Boolean {
        return departure.location == location
    }

    fun distance(): Double = distanceInMeters

    fun departuresAfterOrEqual(at: Instant): Boolean {
        return departure.at.isAfter(at) || departure.at == at
    }
}
