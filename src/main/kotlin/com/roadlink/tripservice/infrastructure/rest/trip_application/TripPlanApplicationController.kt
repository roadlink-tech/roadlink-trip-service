package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.roadlink.tripservice.infrastructure.rest.trip_application.request.CreateTripPlanApplicationRequest
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationPlanResponseFactory
import com.roadlink.tripservice.usecases.CreateTripPlanApplication
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*

@Controller("/trip-service/trip_plan_application")
class TripPlanApplicationController(
    private val createTripPlanApplication: CreateTripPlanApplication,
    private val responseFactory: TripApplicationPlanResponseFactory
) {
    @Post
    fun create(@Body request: CreateTripPlanApplicationRequest): HttpResponse<ApiResponse> {
        val input = request.toDTO()
        val output = createTripPlanApplication(input)
        return responseFactory.from(output)
    }
}
