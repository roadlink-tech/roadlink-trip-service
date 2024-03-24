package com.roadlink.tripservice.infrastructure.rest.trip_plan

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import java.util.*

data class TripPlanResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("trip_legs")
    val tripLegs: List<TripLegResponse>,
    @JsonProperty("passenger_id")
    val passengerId: String,
    @JsonProperty("status")
    val status: String
) : ApiResponse {

    data class TripLegResponse(
        @JsonProperty("id")
        val id: UUID,
        @JsonProperty("trip_id")
        val tripId: UUID,
        @JsonProperty("driver_id")
        val driverId: UUID,
        @JsonProperty("vehicle_id")
        val vehicleId: UUID,
    ) {

        companion object {
            fun from(tripLeg: TripPlan.TripLeg): TripLegResponse {
                return TripLegResponse(
                    id = tripLeg.id,
                    tripId = tripLeg.tripId,
                    driverId = tripLeg.driverId,
                    vehicleId = tripLeg.vehicleId,
                )
            }
        }
    }

    companion object {
        fun from(tripPlan: TripPlan): TripPlanResponse {
            return TripPlanResponse(
                id = tripPlan.id.toString(),
                tripLegs = tripPlan.tripLegs.map { TripLegResponse.from(it) },
                passengerId = tripPlan.passengerId.toString(),
                status = tripPlan.status().toString()
            )
        }
    }
}