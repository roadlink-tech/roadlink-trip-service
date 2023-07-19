package com.roadlink.tripservice.infrastructure.rest.handlers

import com.roadlink.tripservice.infrastructure.rest.mappers.DriverTripDetailResponseMapper
import com.roadlink.tripservice.infrastructure.rest.responses.DriverTripDetailResponse
import com.roadlink.tripservice.usecases.GetDriverTripDetail
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/trip-service")
class GetDriverTripDetailsController(
    private val getDriverTripDetail: GetDriverTripDetail,
) {
    // TODO validate that tripId could be mapped against  UUID
    @Get("/driver-trip-detail")
    fun handle(@QueryValue tripId: String): HttpResponse<DriverTripDetailResponse> {
        val driverTripDetail = getDriverTripDetail(GetDriverTripDetail.Input(tripId = UUID.fromString(tripId)))

        return HttpResponse.ok(DriverTripDetailResponseMapper.map(driverTripDetail))
    }
}