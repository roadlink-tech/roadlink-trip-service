package com.roadlink.tripservice.infrastructure.rest.error.handlers

import com.roadlink.tripservice.domain.common.utils.time.exception.InvalidTripTimeRange

import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponse
import com.roadlink.tripservice.infrastructure.rest.error.ErrorResponseCode
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [InvalidTripTimeRange::class, ExceptionHandler::class])
class InvalidTripTimeRangeHandler : ExceptionHandler<InvalidTripTimeRange, HttpResponse<InvalidTripTimeRangeResponse>> {
    override fun handle(request: HttpRequest<*>, exception: InvalidTripTimeRange): HttpResponse<InvalidTripTimeRangeResponse> {
        return HttpResponse.status<InvalidTripTimeRangeResponse>(HttpStatus.BAD_REQUEST)
            .body(
                InvalidTripTimeRangeResponse(
                actualTripPointEstimatedArrivalTime = exception.actualTripPointEstimatedArrivalTime.toEpochMilli(),
                nextTripPointEstimatedArrivalTime = exception.nextTripPointEstimatedArrivalTime.toEpochMilli(),
            )
            )
    }
}

data class InvalidTripTimeRangeResponse(
    val actualTripPointEstimatedArrivalTime: Long,
    val nextTripPointEstimatedArrivalTime: Long,
) : ErrorResponse(code = ErrorResponseCode.INVALID_TRIP_TIME_RANGE)