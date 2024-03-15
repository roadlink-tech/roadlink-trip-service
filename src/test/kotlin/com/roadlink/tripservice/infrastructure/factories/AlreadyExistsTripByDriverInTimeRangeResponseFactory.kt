package com.roadlink.tripservice.infrastructure.factories

import com.roadlink.tripservice.infrastructure.rest.error.handlers.AlreadyExistsTripByDriverInTimeRangeResponse
import com.roadlink.tripservice.usecases.factory.InstantFactory

object AlreadyExistsTripByDriverInTimeRangeResponseFactory {
    fun avCabildo() =
        AlreadyExistsTripByDriverInTimeRangeResponse(
            driver = "John Smith",
            from = InstantFactory.october15_12hs().toEpochMilli(),
            to = InstantFactory.october15_18hs().toEpochMilli(),
        )
}