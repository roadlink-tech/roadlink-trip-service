package com.roadlink.tripservice.infrastructure.rest.handlers

import com.roadlink.tripservice.infrastructure.rest.mappers.TripResponseMapper
import com.roadlink.tripservice.infrastructure.rest.requests.CreateTripRequest
import com.roadlink.tripservice.infrastructure.rest.responses.TripResponse
import com.roadlink.tripservice.usecases.CreateTrip
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/trip-service/trip")
class CreateTripController(private val createTrip: CreateTrip) {

    @Post
    fun createTrip(@Body request: CreateTripRequest): HttpResponse<TripResponse> {
        val input = request.toDomain()

        val trip = createTrip(input)

        return HttpResponse
            .status<TripResponse>(HttpStatus.CREATED)
            .body(TripResponseMapper.map(trip))
    }
}
