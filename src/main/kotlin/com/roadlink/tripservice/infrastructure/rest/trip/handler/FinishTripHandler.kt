package com.roadlink.tripservice.infrastructure.rest.trip.handler

import com.roadlink.tripservice.infrastructure.rest.trip.response.TripLegFinishedResponse
import com.roadlink.tripservice.infrastructure.rest.trip.response.TripResponse
import com.roadlink.tripservice.usecases.trip.FinishTrip
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.*
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/trip-service/trips/{tripId}/finish")
class FinishTripHandler(private val finishTrip: FinishTrip) {

    companion object {
        private val logger = LoggerFactory.getLogger(FinishTripHandler::class.java)
    }

    @Post
    fun finish(
        @PathVariable("tripId") tripId: String,
    ): HttpResponse<List<TripLegFinishedResponse>> {
        val response = finishTrip(FinishTrip.Input(tripId = UUID.fromString(tripId)))
        logger.info("The following feedbacks solicitudes were created ${response.feedbackSolicitudes}")
        return HttpResponse
            .status<TripResponse>(OK)
            .body(response.tripLegsFinished.map { TripLegFinishedResponse.from(it) })
    }
}