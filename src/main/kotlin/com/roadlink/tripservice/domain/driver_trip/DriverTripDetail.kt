package com.roadlink.tripservice.domain.driver_trip

import com.roadlink.tripservice.domain.trip.TripStatus
import java.util.*

data class DriverTripDetail(
    val tripId: UUID,
    val tripStatus: TripStatus,
    val seatStatus: SeatsAvailabilityStatus,
    val hasPendingApplications: Boolean,
    val sectionDetails: List<DriverSectionDetail>
)
