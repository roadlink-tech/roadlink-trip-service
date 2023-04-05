package com.roadlink.tripservice.trip.infrastructure.rest.responses

data class AlreadyExistsTripByDriverInTimeRangeExpectedResponse(
    val code: String,
    val driver: String,
    val from: Long,
    val to: Long,
)
