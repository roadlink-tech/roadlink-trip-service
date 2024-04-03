package com.roadlink.tripservice.infrastructure.rest.trip.response

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import java.util.*

data class TripLegFinishedResponse(
    val id: UUID,
    val tripId: UUID,
    val status: TripPlan.Status
) {
    companion object {
        fun from(tripLeg: TripPlan.TripLeg): TripLegFinishedResponse {
            return TripLegFinishedResponse(
                id = tripLeg.id,
                tripId = tripLeg.tripId,
                status = tripLeg.status
            )
        }
    }
}