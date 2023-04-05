package com.roadlink.tripservice.infrastructure.rest.responses

data class InvalidTripTimeRangeResponse(
    val actualTripPointEstimatedArrivalTime: Long,
    val nextTripPointEstimatedArrivalTime: Long,
) : ErrorResponse(code = ErrorResponseCode.INVALID_TRIP_TIME_RANGE)
