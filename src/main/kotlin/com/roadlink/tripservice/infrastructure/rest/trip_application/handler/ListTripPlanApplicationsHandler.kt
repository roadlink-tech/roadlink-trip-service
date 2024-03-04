package com.roadlink.tripservice.infrastructure.rest.trip_application.handler

import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripPlanApplicationResponse
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_application.plan.ListTripPlanApplications
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/trip-service/users/{userId}")
class ListTripPlanApplicationsHandler(
    private val listTripPlanApplications: UseCase<ListTripPlanApplications.Input, ListTripPlanApplications.Output>,
) {
    @Get("/trip_plan_applications")
    fun list(
        @PathVariable("userId") passengerId: String,
        @QueryValue("status") status: String? = null,
    ): HttpResponse<List<TripPlanApplicationResponse>> {
        val response = listTripPlanApplications(
            ListTripPlanApplications.Input(
                passengerId = UUID.fromString(passengerId),
                tripApplicationStatus = status
            )
        )

        return HttpResponse.ok(response.tripPlanApplications.map {
            TripPlanApplicationResponse.from(it)
        })
    }
}