package com.roadlink.tripservice.domain.time.exception

import java.time.Instant

class InvalidTripTimeRange(
    val actualTripPointEstimatedArrivalTime: Instant,
    val nextTripPointEstimatedArrivalTime: Instant,
) : RuntimeException("Invalid trip time range: ($actualTripPointEstimatedArrivalTime, $nextTripPointEstimatedArrivalTime)")
