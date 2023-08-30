package com.roadlink.tripservice.infrastructure.rest.responses

import java.util.*

data class DriverTripDetailResponse(
    val tripId: UUID,
    val tripStatus: TripStatusResponse,
    val seatStatus: SeatsAvailabilityStatusResponse,
    val hasPendingApplications: Boolean,
    val sectionDetails: List<DriverSectionDetailResponse>
)
