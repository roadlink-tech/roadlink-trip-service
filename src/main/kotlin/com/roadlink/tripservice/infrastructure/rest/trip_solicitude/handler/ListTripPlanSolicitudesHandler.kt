package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripPlanApplicationResponse
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_solicitude.plan.ListTripPlanSolicitudes
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/trip-service/users/{userId}")
class ListTripPlanSolicitudesHandler(
    private val listTripPlanSolicitudes: UseCase<ListTripPlanSolicitudes.Input, ListTripPlanSolicitudes.Output>,
) {
    @Get("/trip_plan_applications")
    fun list(
        @PathVariable("userId") passengerId: String,
        @QueryValue("status") status: String? = null,
    ): HttpResponse<List<TripPlanApplicationResponse>> {
        val response = listTripPlanSolicitudes(
            ListTripPlanSolicitudes.Input(
                passengerId = UUID.fromString(passengerId),
                status = status
            )
        )

        return HttpResponse.ok(response.tripPlanSolicitudes.map {
            TripPlanApplicationResponse.from(it)
        })
    }
}