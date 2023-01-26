package com.roadlink.tripservice.trip.domain

import java.time.Instant

sealed class Event {
    abstract val at: Instant
}
