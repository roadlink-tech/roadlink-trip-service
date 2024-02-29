package com.roadlink.tripservice.infrastructure.rest.trip_search.handler

import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse
import com.roadlink.tripservice.infrastructure.rest.trip_search.mapper.SearchTripResponseMapper
import com.roadlink.tripservice.usecases.trip.SearchTrip
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import java.time.Instant

@Controller("/trip-service/trips")
class SearchTripRestController(private val searchTrip: SearchTrip) {
    @Get
    fun handle(
        @QueryValue departureLatitude: Double,
        @QueryValue departureLongitude: Double,
        @QueryValue arrivalLatitude: Double,
        @QueryValue arrivalLongitude: Double,
        @QueryValue at: Long,
    ): HttpResponse<SearchTripResponse> {
        val input = SearchTrip.Input(
            departure = Location(
                latitude = departureLatitude,
                longitude = departureLongitude,
            ),
            arrival = Location(
                latitude = arrivalLatitude,
                longitude = arrivalLongitude,
            ),
            at = Instant.ofEpochMilli(at),
        )

        val tripPlans = searchTrip(input)

        return HttpResponse.ok(SearchTripResponseMapper.map(tripPlans))
    }
}
