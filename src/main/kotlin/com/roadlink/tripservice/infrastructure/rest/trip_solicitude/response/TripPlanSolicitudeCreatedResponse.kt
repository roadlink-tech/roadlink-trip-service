package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.usecases.trip_solicitude.plan.CreateTripPlanSolicitude
import java.util.*

data class TripPlanSolicitudeCreatedResponse(
    val tripPlanSolicitudeId: UUID
) : ApiResponse {
    companion object {
        fun from(output: CreateTripPlanSolicitude.Output.TripPlanSolicitudeCreated): TripPlanSolicitudeCreatedResponse {
            return TripPlanSolicitudeCreatedResponse(
                tripPlanSolicitudeId = output.tripPlanSolicitudeId
            )
        }
    }
}