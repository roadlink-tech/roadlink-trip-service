package com.roadlink.tripservice.infrastructure.rest.driver_trip.handler

import com.roadlink.tripservice.infrastructure.rest.driver_trip.mapper.DriverTripLegSolicitudeResponseMapper
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripLegSolicitudeResponse
import com.roadlink.tripservice.usecases.driver_trip.ListDriverTripLegSolicitudes
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.util.*
import kotlin.collections.List

@Controller("/trip-service")
class ListDriverTripLegSolicitudesHandler(
    private val listDriverTripLegSolicitudes: ListDriverTripLegSolicitudes,
) {
    @Get("/driver_trip_leg_solicitudes")
    fun handle(@QueryValue tripId: String): HttpResponse<List<DriverTripLegSolicitudeResponse>> {
        val driverTripLegSolicitudes = listDriverTripLegSolicitudes(
            ListDriverTripLegSolicitudes.Input(
                tripId = UUID.fromString(tripId)
            )
        )

        return HttpResponse.ok(driverTripLegSolicitudes.map {
            DriverTripLegSolicitudeResponseMapper.map(it)
        })
    }
}