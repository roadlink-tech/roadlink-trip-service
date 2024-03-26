package com.roadlink.tripservice.infrastructure.rest.trip_plan

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip_plan.TripLegSection
import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponse
import com.roadlink.tripservice.infrastructure.rest.common.trip_point.TripPointResponseMapper
import com.roadlink.tripservice.usecases.trip_plan.ListTripPlan
import com.roadlink.tripservice.usecases.trip_plan.RetrieveTripPlan
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/trip-service")
class TripPlanRestHandler(
    private val listTripPlan: ListTripPlan,
    private val retrieveTripPlan: RetrieveTripPlan,
) {

    @Get("/users/{userId}/trip_plans")
    fun list(
        @PathVariable("userId") passengerId: String,
        @QueryValue("status") status: List<TripPlan.Status> = emptyList(),
    ): HttpResponse<List<TripPlanResponse>> {
        val response = listTripPlan(ListTripPlan.Input(passengerId = UUID.fromString(passengerId), status = status))
        return HttpResponse.ok(response.tripPlans.map {
            TripPlanResponse.from(it)
        })
    }

    @Get("/trip_plans/{tripPlanId}")
    fun list(
        @PathVariable("tripPlanId") tripPlanId: String,
    ): HttpResponse<*> {
        val response = retrieveTripPlan(RetrieveTripPlan.Input(tripPlanId = UUID.fromString(tripPlanId)))
        return when (response) {
            is RetrieveTripPlan.Output.TripPlanFound -> HttpResponse.ok(TripPlanResponse.from(response.tripPlan))
            RetrieveTripPlan.Output.TripPlanNotFound -> HttpResponse.notFound<Unit>()
        }
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
        @JsonProperty("sections")
        val sections: List<TripLegSectionResponse>,
    ) {

        companion object {
            fun from(tripLeg: TripPlan.TripLeg): TripLegResponse {
                return TripLegResponse(
                    id = tripLeg.id,
                    tripId = tripLeg.tripId,
                    driverId = tripLeg.driverId,
                    vehicleId = tripLeg.vehicleId,
                    sections = tripLeg.sections.map { TripLegSectionResponse.from(it) }
                )
            }
        }
    }

    data class TripLegSectionResponse(
        val id: String,
        val departure: TripPointResponse,
        val arrival: TripPointResponse,
    ) {
        companion object {
            fun from(tripLegSection: TripLegSection): TripLegSectionResponse =
                TripLegSectionResponse(
                    id = tripLegSection.id,
                    departure = TripPointResponseMapper.map(tripLegSection.departure),
                    arrival = TripPointResponseMapper.map(tripLegSection.departure),
                )
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
