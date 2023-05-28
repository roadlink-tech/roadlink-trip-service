package com.roadlink.tripservice.infrastructure.rest.trip_application.response

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput
import java.util.*

data class TripPlanApplicationCreatedResponse(
    val tripPlanApplicationId: UUID
) : ApiResponse {
    companion object {
        fun from(output: CreateTripPlanApplicationOutput.TripPlanApplicationCreated): TripPlanApplicationCreatedResponse {
            return TripPlanApplicationCreatedResponse(
                tripPlanApplicationId = output.tripPlanApplicationId
            )
        }
    }
}