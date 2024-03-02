package com.roadlink.tripservice.infrastructure.rest.responses

data class AlreadyExistsTripByDriverInTimeRangeExpectedResponse(
    val code: String,
    val driver: String,
    val from: Long,
    val to: Long,
)
