package com.roadlink.tripservice.infrastructure.rest.responses

class AlreadyExistsTripByDriverInTimeRangeResponse(
    val driver: String,
    val from: Long,
    val to: Long,
) : ErrorResponse(code = ErrorResponseCode.ALREADY_EXISTS_TRIP_BY_DRIVER_IN_TIME_RANGE)