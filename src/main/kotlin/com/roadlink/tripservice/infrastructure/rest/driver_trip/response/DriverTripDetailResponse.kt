package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import java.util.*

data class DriverTripDetailResponse(
    val tripId: UUID,
    val tripStatus: DriverTripStatusResponse,
    val seatStatus: SeatsAvailabilityStatusResponse,
    val hasPendingApplications: Boolean,
    val sectionDetails: List<DriverSectionDetailResponse>
)

enum class DriverTripStatusResponse { NOT_STARTED, IN_PROGRESS, FINISHED }