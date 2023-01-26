package com.roadlink.tripservice.trip

import com.roadlink.tripservice.trip.domain.TimeProvider
import java.time.Instant

class StubTimeProvider(private val fixedNow: Instant) : TimeProvider {
    override fun now(): Instant = fixedNow
}