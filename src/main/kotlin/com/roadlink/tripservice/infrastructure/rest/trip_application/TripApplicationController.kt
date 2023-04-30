package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationPlanResponseFactory
import com.roadlink.tripservice.usecases.trip_plan.AcceptTripApplication
import io.micronaut.http.annotation.Controller
import com.roadlink.tripservice.usecases.trip_plan.RejectTripApplication
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Put
import java.util.*

@Controller("/trip-service/trip_application")
class TripApplicationController(
    private val rejectTripApplication: RejectTripApplication,
    private val acceptTripApplication: AcceptTripApplication,
    private val responseFactory: TripApplicationPlanResponseFactory,
) {
    @Put("/{id}/non-acceptance")
    fun nonAcceptance(@PathVariable id: String): HttpResponse<ApiResponse> {
        val output = rejectTripApplication(UUID.fromString(id))
        return responseFactory.from(output)
    }

    @Put("/{id}/acceptance")
    fun acceptance(@PathVariable id: String): HttpResponse<ApiResponse> {
        val output = acceptTripApplication(UUID.fromString(id))
        return responseFactory.from(output)
    }
}