package com.roadlink.tripservice.infrastructure.rest.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
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
}
