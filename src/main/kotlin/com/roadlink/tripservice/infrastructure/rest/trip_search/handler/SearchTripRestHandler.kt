package com.roadlink.tripservice.infrastructure.rest.trip_search.handler

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.infrastructure.rest.trip_search.mapper.SearchTripResponseMapper
import com.roadlink.tripservice.infrastructure.rest.trip_search.response.SearchTripResponse
import com.roadlink.tripservice.usecases.UseCase
import com.roadlink.tripservice.usecases.trip_search.SearchTrip
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.QueryValue
import java.time.Instant
import java.util.*

@Controller("/trip-service/trips")
class SearchTripRestHandler(private val searchTrip: UseCase<SearchTrip.Input, SearchTrip.Output>) {
    @Get
    fun handle(
        @QueryValue departureLatitude: Double,
        @QueryValue departureLongitude: Double,
        @QueryValue arrivalLatitude: Double,
        @QueryValue arrivalLongitude: Double,
        @QueryValue filter: List<String> = emptyList(),
        @QueryValue at: Long,
        @Header("X-Caller-Id") callerId: String
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
            callerId = UUID.fromString(callerId),
            filters = filter
        )

        val tripPlans = searchTrip(input)

        return HttpResponse.ok(SearchTripResponseMapper.map(tripPlans.result))
    }
}
