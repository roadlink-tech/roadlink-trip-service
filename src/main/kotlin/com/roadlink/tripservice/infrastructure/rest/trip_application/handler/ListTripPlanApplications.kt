package com.roadlink.tripservice.infrastructure.rest.trip_application.handler

import com.roadlink.tripservice.infrastructure.rest.trip_application.mapper.DriverTripApplicationResponseMapper
import com.roadlink.tripservice.infrastructure.rest.trip_application.response.DriverTripApplicationResponse
import com.roadlink.tripservice.usecases.trip_application.RetrieveDriverTripApplications
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/trip-service/users/{userId}")
class ListTripPlanApplications(
    private val retrieveDriverTripApplications: RetrieveDriverTripApplications,
) {
    @Get("/trip_applications")
    fun list(@QueryValue tripId: String): HttpResponse<List<DriverTripApplicationResponse>> {
        val driverTripApplications = retrieveDriverTripApplications(
            RetrieveDriverTripApplications.Input(
                tripId = UUID.fromString(tripId)
            ))

        return HttpResponse.ok(driverTripApplications.map {
            DriverTripApplicationResponseMapper.map(it)
        })
    }
}