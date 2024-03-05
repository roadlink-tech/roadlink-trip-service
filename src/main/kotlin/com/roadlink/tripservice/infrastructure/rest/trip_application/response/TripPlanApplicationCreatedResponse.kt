package com.roadlink.tripservice.infrastructure.rest.trip_application.response

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplication
import java.util.*

data class TripPlanApplicationCreatedResponse(
    val tripPlanApplicationId: UUID
) : ApiResponse {
    companion object {
        fun from(output: CreateTripPlanApplication.Output.TripPlanApplicationCreated): TripPlanApplicationCreatedResponse {
            return TripPlanApplicationCreatedResponse(
                tripPlanApplicationId = output.tripPlanApplicationId
            )
        }
    }
}