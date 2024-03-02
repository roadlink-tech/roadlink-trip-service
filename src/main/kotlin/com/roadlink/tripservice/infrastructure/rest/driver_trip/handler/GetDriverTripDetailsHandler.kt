package com.roadlink.tripservice.infrastructure.rest.driver_trip.handler

import com.roadlink.tripservice.infrastructure.rest.driver_trip.mapper.DriverTripDetailResponseMapper
import com.roadlink.tripservice.infrastructure.rest.driver_trip.response.DriverTripDetailResponse
import com.roadlink.tripservice.usecases.driver_trip.RetrieveDriverTripDetail
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/trip-service")
class GetDriverTripDetailsHandler(
    private val retrieveDriverTripDetail: RetrieveDriverTripDetail,
) {
    // TODO validate that tripId could be mapped against  UUID
    @Get("/driver-trip-detail")
    fun handle(@QueryValue tripId: String): HttpResponse<DriverTripDetailResponse> {
        val driverTripDetail = retrieveDriverTripDetail(RetrieveDriverTripDetail.Input(tripId = UUID.fromString(tripId)))
        return HttpResponse.ok(DriverTripDetailResponseMapper.map(driverTripDetail))
    }
}