package com.roadlink.tripservice.infrastructure.rest.trip.handler

import com.roadlink.tripservice.config.user.UserRepositoryConfig
import com.roadlink.tripservice.usecases.trip.StartTrip
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/trip-service/trips/{tripId}/start")
class StartTripHandler(private val startTrip: StartTrip) {

    companion object {
        private val logger = LoggerFactory.getLogger(StartTripHandler::class.java)
    }

    @Post
    fun start(
        @PathVariable("tripId") tripId: String,
    ): HttpResponse<Any> {
        val response = startTrip(StartTrip.Input(tripId = UUID.fromString(tripId)))
        logger.info("The following trip plan applications were rejected ${response.rejectedTripPlanSolicitudes.map { it.id }}")
        return HttpResponse.ok()
    }
}