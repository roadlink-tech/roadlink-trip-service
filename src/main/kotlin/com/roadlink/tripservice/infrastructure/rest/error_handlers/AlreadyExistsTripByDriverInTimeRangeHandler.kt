package com.roadlink.tripservice.infrastructure.rest.error_handlers

import com.roadlink.tripservice.domain.AlreadyExistsTripByDriverInTimeRange
import com.roadlink.tripservice.infrastructure.rest.responses.AlreadyExistsTripByDriverInTimeRangeResponse
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [AlreadyExistsTripByDriverInTimeRange::class, ExceptionHandler::class])
class AlreadyExistsTripByDriverInTimeRangeHandler : ExceptionHandler<AlreadyExistsTripByDriverInTimeRange, HttpResponse<AlreadyExistsTripByDriverInTimeRangeResponse>> {
    override fun handle(request: HttpRequest<*>, exception: AlreadyExistsTripByDriverInTimeRange): HttpResponse<AlreadyExistsTripByDriverInTimeRangeResponse> {
        return HttpResponse.status<AlreadyExistsTripByDriverInTimeRangeResponse>(HttpStatus.CONFLICT)
            .body(AlreadyExistsTripByDriverInTimeRangeResponse(
                driver = exception.driver,
                from = exception.timeRange.from.toEpochMilli(),
                to = exception.timeRange.to.toEpochMilli(),
            ))
    }
}