package com.fdt.tripservice.domain

data class Section(val departure: TripPoint, val arrival: TripPoint, val distanceInMeters: Double) {

    fun departure(): Location = departure.location

    fun arrival(): Location = arrival.location

    fun arrivesTo(location: Location): Boolean {
        return arrival.location == location
    }

    fun departuresFrom(location: Location): Boolean {
        return departure.location == location
    }

    fun distance(): Double = distanceInMeters
}
