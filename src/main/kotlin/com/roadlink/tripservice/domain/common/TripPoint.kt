package com.roadlink.tripservice.domain.common

import com.roadlink.tripservice.domain.common.address.Address
import java.time.Instant

// TODO when we use it in departureInstant's departure attribute the name "estimatedArrivalTime" does not make sense
data class TripPoint(
    val estimatedArrivalTime: Instant,
    val address: Address,
)
