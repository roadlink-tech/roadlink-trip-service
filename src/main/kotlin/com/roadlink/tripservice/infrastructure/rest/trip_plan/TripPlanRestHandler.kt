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
        val response = listTripPlan(
            ListTripPlan.Input(
                passengerId = UUID.fromString(passengerId),
                status = status
            )
        )
        return HttpResponse.ok(response.tripPlans.map {
            TripPlanResponse.from(it)
        })
    }

    @Get("/trip_plans/{tripPlanId}")
    fun list(
        @PathVariable("tripPlanId") tripPlanId: String,
    ): HttpResponse<*> {
        return when (val response =
            retrieveTripPlan(RetrieveTripPlan.Input(tripPlanId = UUID.fromString(tripPlanId)))) {
            is RetrieveTripPlan.Output.TripPlanFound -> HttpResponse.ok(TripPlanResponse.from(response.tripPlan))
            RetrieveTripPlan.Output.TripPlanNotFound -> HttpResponse.notFound<Unit>()
        }
    }

}
