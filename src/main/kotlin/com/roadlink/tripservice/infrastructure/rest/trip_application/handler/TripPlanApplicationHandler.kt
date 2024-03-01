package com.roadlink.tripservice.infrastructure.rest.trip_application.handler

import com.roadlink.tripservice.infrastructure.rest.trip_application.request.CreateTripPlanApplicationRequest
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripPlanApplicationResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplicationInput
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplicationOutput
import com.roadlink.tripservice.usecases.trip_application.plan.GetTripPlanApplicationInput
import com.roadlink.tripservice.usecases.trip_application.plan.GetTripPlanApplicationOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*

@Controller("/trip-service/trip_plan_application")
class TripPlanApplicationHandler(
    private val createTripPlanApplication: UseCase<CreateTripPlanApplicationInput, CreateTripPlanApplicationOutput>,
    private val getTripPlanApplication: UseCase<GetTripPlanApplicationInput, GetTripPlanApplicationOutput>,
    private val responseFactory: TripPlanApplicationResponseFactory
) {
    @Post
    fun create(@Body request: CreateTripPlanApplicationRequest): HttpResponse<*> {
        val input = request.toInput()
        val output = createTripPlanApplication(input)
        return responseFactory.from(output)
    }

    @Get("/{tripPlanApplicationId}")
    fun get(@PathVariable("tripPlanApplicationId") tripPlanApplicationId: String): HttpResponse<*> {
        val input = GetTripPlanApplicationInput(tripPlanApplicationId)
        val output = getTripPlanApplication(input)
        return responseFactory.from(output)
    }
}
