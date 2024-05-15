package com.roadlink.tripservice.domain.driver_trip

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripStatus
import java.util.*

data class DriverTripDetail(
    val tripId: UUID,
    val tripStatus: Trip.Status,
    val seatStatus: SeatsAvailabilityStatus,
    val hasPendingApplications: Boolean,
    val sectionDetails: List<DriverSectionDetail>
)
