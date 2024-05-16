package com.roadlink.tripservice.infrastructure.rest.driver_trip.response

import com.roadlink.tripservice.domain.trip.Trip
import java.util.*

data class DriverTripDetailResponse(
    val tripId: UUID,
    val tripStatus: DriverTripStatusResponse,
    val seatStatus: SeatsAvailabilityStatusResponse,
    val hasPendingApplications: Boolean,
    val sectionDetails: List<DriverSectionDetailResponse>
)

enum class DriverTripStatusResponse {
    NOT_STARTED, IN_PROGRESS, FINISHED;

    companion object {

        fun from(status: Trip.Status): DriverTripStatusResponse {
            return when (status) {
                Trip.Status.NOT_STARTED -> NOT_STARTED
                Trip.Status.IN_PROGRESS -> IN_PROGRESS
                Trip.Status.FINISHED -> FINISHED
            }
        }
    }
}