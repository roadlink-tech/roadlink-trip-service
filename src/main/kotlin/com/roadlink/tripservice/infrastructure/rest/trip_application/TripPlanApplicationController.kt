package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.roadlink.tripservice.infrastructure.rest.trip_application.request.CreateTripPlanApplicationRequest
import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationPlanResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationInput
import com.roadlink.tripservice.usecases.trip_plan.CreateTripPlanApplicationOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*

@Controller("/trip-service/trip_plan_application")
class TripPlanApplicationController(
    private val createTripPlanApplication: UseCase<CreateTripPlanApplicationInput, CreateTripPlanApplicationOutput>,
    private val responseFactory: TripApplicationPlanResponseFactory
) {
    @Post
    fun create(@Body request: CreateTripPlanApplicationRequest): HttpResponse<ApiResponse> {
        val input = request.toInput()
        val output = createTripPlanApplication(input)
        return responseFactory.from(output)
    }
}
