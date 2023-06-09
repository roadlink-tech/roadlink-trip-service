package com.roadlink.tripservice.domain

data class DriverTripDetail(
    val tripId: String,
    val tripStatus: TripStatus,
    val seatStatus: SeatsAvailabilityStatus,
    val sectionDetails: List<DriverSectionDetail>
)
