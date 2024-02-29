package com.roadlink.tripservice.infrastructure.rest.trip.handler

import com.roadlink.tripservice.infrastructure.rest.trip.mapper.TripResponseMapper
import com.roadlink.tripservice.infrastructure.rest.trip.request.CreateTripRequest
import com.roadlink.tripservice.infrastructure.rest.trip.response.TripResponse
import com.roadlink.tripservice.usecases.trip.CreateTrip
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

