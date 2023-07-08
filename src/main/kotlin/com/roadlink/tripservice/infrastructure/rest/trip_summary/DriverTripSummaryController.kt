package com.roadlink.tripservice.infrastructure.rest.trip_summary

import com.roadlink.tripservice.infrastructure.rest.ApiResponse
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_summary.RetrieveDriverTripSummaryOutput
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.util.*


@Controller("/trip-service/driver/{driverId}/trip-summary")
class DriverTripSummaryController(
    private val retrieveDriverTripSummary: UseCase<UUID, RetrieveDriverTripSummaryOutput>,
    private val responseFactory: DriverTripSummaryResponseFactory
) {

    @Get
    fun retrieve(@PathVariable driverId: String): HttpResponse<Any> {
        val output = retrieveDriverTripSummary(UUID.fromString(driverId))
        return responseFactory.from(output)
    }

}