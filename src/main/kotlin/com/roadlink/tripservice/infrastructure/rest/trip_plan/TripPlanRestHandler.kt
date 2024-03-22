package com.roadlink.tripservice.infrastructure.rest.trip_plan

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.usecases.trip_plan.ListTripPlan
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/trip-service/users/{userId}")
class TripPlanRestHandler(private val listTripPlan: ListTripPlan) {

    @Get("/trip_plans")
    fun list(
        @PathVariable("userId") passengerId: String,
        @QueryValue("status") status: List<TripPlan.Status> = emptyList(),
    ): HttpResponse<List<TripPlanResponse>> {
        val response = listTripPlan(ListTripPlan.Input(passengerId = UUID.fromString(passengerId), status = status))
        return HttpResponse.ok(response.tripPlans.map {
            TripPlanResponse.from(it)
        })

    }
}

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
