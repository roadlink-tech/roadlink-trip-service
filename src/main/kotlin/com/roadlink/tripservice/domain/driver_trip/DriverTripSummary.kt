package com.roadlink.tripservice.domain.driver_trip

import com.roadlink.tripservice.domain.trip.Trip


data class DriverTripSummary(
    val trip: Trip,
    val hasAvailableSeats: Boolean,
    val hasPendingApplications: Boolean
)