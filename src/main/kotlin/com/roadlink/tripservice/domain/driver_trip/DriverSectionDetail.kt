package com.roadlink.tripservice.domain.driver_trip

import com.roadlink.tripservice.domain.PassengerResult
import com.roadlink.tripservice.domain.trip.TripPoint

data class DriverSectionDetail(
    val sectionId: String,
    val departure: TripPoint,
    val arrival: TripPoint,
    val occupiedSeats: Int,
    val availableSeats: Int,
    val passengers: List<PassengerResult>,
)