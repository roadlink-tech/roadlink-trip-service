package com.roadlink.tripservice.infrastructure.rest.responses

data class DriverTripDetailResponse(
    val tripId: String,
    val tripStatus: TripStatusResponse,
    val seatStatus: SeatsAvailabilityStatusResponse,
    val sectionDetails: List<DriverSectionDetailResponse>
)
