package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationPlanResponseFactory
import io.micronaut.http.annotation.Controller
import com.roadlink.tripservice.usecases.RejectTripApplication
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Put
import java.util.*

@Controller("/trip-service/trip_application")
class TripApplicationController(
    private val rejectTripPlanApplication: RejectTripApplication,
    private val responseFactory: TripApplicationPlanResponseFactory,
) {
    @Put("/{id}/non-acceptance")
    fun nonAcceptance(@PathVariable id: String): HttpResponse<ApiResponse> {
        val output = rejectTripPlanApplication(UUID.fromString(id))
        return responseFactory.from(output)
    }
}