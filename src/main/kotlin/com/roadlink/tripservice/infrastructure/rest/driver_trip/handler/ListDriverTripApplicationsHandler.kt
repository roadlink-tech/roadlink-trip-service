package com.roadlink.tripservice.infrastructure.rest.driver_trip.handler

import com.roadlink.tripservice.infrastructure.rest.driver_trip.mapper.DriverTripApplicationResponseMapper
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripApplicationResponse
import com.roadlink.tripservice.usecases.driver_trip.ListDriverTripApplications
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.util.*
import kotlin.collections.List

@Controller("/trip-service")
class ListDriverTripApplicationsHandler(
    private val listDriverTripApplications: ListDriverTripApplications,
) {
    @Get("/driver-trip-applications")
    fun handle(@QueryValue tripId: String): HttpResponse<List<DriverTripApplicationResponse>> {
        val driverTripApplications = listDriverTripApplications(
            ListDriverTripApplications.Input(
            tripId = UUID.fromString(tripId)
        ))

        return HttpResponse.ok(driverTripApplications.map {
            DriverTripApplicationResponseMapper.map(it)
        })
    }
}