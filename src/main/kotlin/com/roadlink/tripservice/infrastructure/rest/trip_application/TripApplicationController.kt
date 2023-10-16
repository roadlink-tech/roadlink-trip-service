package com.roadlink.tripservice.infrastructure.rest.trip_application

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.TripApplicationPlanResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_plan.*
import io.micronaut.http.annotation.Controller
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Put
import java.util.*

// TODO unificar convensión y reemplazar "-" por "_" en los endpoints
@Controller("/trip-service/trip_application")
class TripApplicationController(
    private val rejectTripApplication: UseCase<UUID, RejectTripApplicationOutput>,
    private val acceptTripApplication: UseCase<AcceptTripApplicationInput, AcceptTripApplicationOutput>,
    private val responseFactory: TripApplicationPlanResponseFactory,
) {
    @Put("/{id}/non-acceptance")
    fun nonAcceptance(@PathVariable id: String): HttpResponse<ApiResponse> {
        val output = rejectTripApplication(UUID.fromString(id))
        return responseFactory.from(output)
    }

    @Put("/{id}/acceptance")
    fun acceptance(
        @PathVariable id: String,
        @Header("X-Caller-Id") callerId: String
    ): HttpResponse<ApiResponse> {
        val output = acceptTripApplication(
            AcceptTripApplicationInput(
                tripApplication = UUID.fromString(id),
                callerId = UUID.fromString(callerId)
            )
        )
        return responseFactory.from(output)
    }
}