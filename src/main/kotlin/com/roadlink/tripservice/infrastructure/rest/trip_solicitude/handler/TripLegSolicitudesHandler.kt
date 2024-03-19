package com.roadlink.tripservice.infrastructure.rest.trip_solicitude.handler

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.infrastructure.rest.trip_solicitude.response.TripPlanSolicitudeResponseFactory
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_solicitude.AcceptTripLegSolicitudeInput
import com.roadlink.tripservice.usecases.trip_solicitude.AcceptTripLegSolicitudeOutput
import com.roadlink.tripservice.usecases.trip_solicitude.RejectTripLegSolicitudeOutput
import io.micronaut.http.annotation.Controller
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Put
import java.util.*

// TODO unificar convensión y reemplazar "-" por "_" en los endpoints
@Controller("/trip-service/trip_leg_solicitudes")
class TripLegSolicitudesHandler(
    private val rejectTripApplication: UseCase<UUID, RejectTripLegSolicitudeOutput>,
    private val acceptTripApplication: UseCase<AcceptTripLegSolicitudeInput, AcceptTripLegSolicitudeOutput>,
    private val responseFactory: TripPlanSolicitudeResponseFactory,
) {
    @Put("/{id}/reject")
    fun nonAcceptance(@PathVariable id: String): HttpResponse<ApiResponse> {
        val output = rejectTripApplication(UUID.fromString(id))
        return responseFactory.from(output)
    }

    @Put("/{id}/accept")
    fun acceptance(
        @PathVariable id: String,
        @Header("X-Caller-Id") callerId: String? = null
    ): HttpResponse<ApiResponse> {
        val output = acceptTripApplication(
            AcceptTripLegSolicitudeInput(
                tripLegSolicitudeId = UUID.fromString(id),
                callerId = callerId?.let { UUID.fromString(it) }
            )
        )
        return responseFactory.from(output)
    }
}