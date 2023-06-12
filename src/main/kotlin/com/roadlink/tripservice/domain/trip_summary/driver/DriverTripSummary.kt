package com.roadlink.tripservice.domain.trip_summary.driver

import com.roadlink.tripservice.domain.trip.Trip

data class DriverTripSummary(
    val trip: Trip,
    val hasAvailableSeats: Boolean,
    val hasPendingApplications: Boolean
)