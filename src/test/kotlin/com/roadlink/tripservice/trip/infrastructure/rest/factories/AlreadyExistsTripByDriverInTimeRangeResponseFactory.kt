package com.roadlink.tripservice.trip.infrastructure.rest.factories

import com.roadlink.tripservice.trip.domain.InstantFactory
import com.roadlink.tripservice.trip.infrastructure.rest.responses.AlreadyExistsTripByDriverInTimeRangeExpectedResponse

object AlreadyExistsTripByDriverInTimeRangeResponseFactory {
    fun avCabildo() =
        AlreadyExistsTripByDriverInTimeRangeExpectedResponse(
            code = "ALREADY_EXISTS_TRIP_BY_DRIVER_IN_TIME_RANGE",
            driver = "John Smith",
            from = InstantFactory.october15_12hs().toEpochMilli(),
            to = InstantFactory.october15_18hs().toEpochMilli(),
        )
}