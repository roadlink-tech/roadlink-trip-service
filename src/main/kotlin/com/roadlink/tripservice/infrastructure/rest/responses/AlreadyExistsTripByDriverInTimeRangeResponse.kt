package com.roadlink.tripservice.infrastructure.rest.responses

import com.roadlink.tripservice.infrastructure.rest.responses.ErrorResponseCode.*

class AlreadyExistsTripByDriverInTimeRangeResponse(
    val driver: String,
    val from: Long,
    val to: Long,
) : ErrorResponse(code = ALREADY_EXISTS_TRIP_BY_DRIVER_IN_TIME_RANGE)