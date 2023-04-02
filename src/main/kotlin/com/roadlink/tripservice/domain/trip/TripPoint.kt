package com.roadlink.tripservice.domain.trip

import java.time.Instant

data class TripPoint(
    val estimatedArrivalTime: Instant,
    val address: Address,
)
