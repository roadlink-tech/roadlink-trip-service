package com.roadlink.tripservice.infrastructure.rest.responses

import com.roadlink.tripservice.infrastructure.rest.responses.ErrorResponseCode.*

data class InvalidTripTimeRangeResponse(
    val actualTripPointEstimatedArrivalTime: Long,
    val nextTripPointEstimatedArrivalTime: Long,
) : ErrorResponse(code = INVALID_TRIP_TIME_RANGE)
