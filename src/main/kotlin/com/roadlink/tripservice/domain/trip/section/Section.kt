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

    fun departure(): Location = departure.address.location

    fun arrival(): Location = arrival.address.location

    fun arrivesTo(location: Location): Boolean {
        return arrival.address.location == location
    }

    fun departuresFrom(location: Location): Boolean {
        return departure.address.location == location
    }

    fun distance(): Double = distanceInMeters

    fun departuresAfterOrEqual(at: Instant): Boolean {
        return departure.estimatedArrivalTime.isAfter(at) || departure.estimatedArrivalTime == at
    }
}
