package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.request.CreateTripPlanSolicitudeRequest
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripPlanSolicitudeResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_solicitude.plan.*

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*

@Controller("/trip-service/trip_plan_solicitudes")
class TripPlanSolicitudesHandler(
    private val createTripPlanSolicitude: UseCase<CreateTripPlanSolicitude.Input, CreateTripPlanSolicitude.Output>,
    private val getTripPlanApplication: UseCase<RetrieveTripPlanSolicitudeInput, RetrieveTripPlanSolicitudeOutput>,
    private val responseFactory: TripPlanSolicitudeResponseFactory

) {
    @Post
    fun create(@Body request: CreateTripPlanSolicitudeRequest): HttpResponse<*> {
        val input = request.toInput()
        val output = createTripPlanSolicitude(input)
        return responseFactory.from(output)
    }

    @Get("/{tripPlanSolicitudeId}")
    fun get(@PathVariable("tripPlanSolicitudeId") tripPlanApplicationId: String): HttpResponse<*> {
        val input = RetrieveTripPlanSolicitudeInput(tripPlanApplicationId)
        val output = getTripPlanApplication(input)
        return responseFactory.from(output)
    }

}
