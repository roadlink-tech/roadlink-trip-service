package com.fdt.tripservice.application.dto

import com.fdt.tripservice.domain.trip.Location
import com.fdt.tripservice.domain.trip.Trip
import java.time.LocalDate

data class TripDto(
        val departure: Location,
        val arrival: Location,
        val departureAt: LocalDate,
        val creatorId: Long,
        val meetingPoints: List<Location>,
        val capacity: Int
) {
    fun toTrip(): Trip {
        return Trip(
                departure = departure,
                arrival = arrival,
                departureAt = departureAt,
                meetingPoints = meetingPoints,
                creatorId = creatorId,
                capacity = capacity
        )
    }
}