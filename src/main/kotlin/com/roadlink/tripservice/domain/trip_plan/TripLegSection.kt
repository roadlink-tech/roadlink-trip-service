package com.roadlink.tripservice.domain.trip_plan

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.common.TripPoint

data class TripLegSection(
    val id: String,
    val departure: TripPoint,
    val arrival: TripPoint,
    val distanceInMeters: Double,
) {
    fun departure(): Location = departure.address.location
    fun arrival(): Location = arrival.address.location
}