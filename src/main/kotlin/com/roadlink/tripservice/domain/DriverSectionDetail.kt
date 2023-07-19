package com.roadlink.tripservice.domain

import com.roadlink.tripservice.domain.trip.TripPoint

data class DriverSectionDetail(
    val sectionId: String,
    val departure: TripPoint,
    val arrival: TripPoint,
    val occupiedSeats: Int,
    val availableSeats: Int,
    val passengers: List<PassengerResult>,
)