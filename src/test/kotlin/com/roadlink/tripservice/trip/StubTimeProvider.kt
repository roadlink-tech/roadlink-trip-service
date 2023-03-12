package com.roadlink.tripservice.trip

import com.roadlink.tripservice.domain.time.TimeProvider
import java.time.Instant

class StubTimeProvider(private val fixedNow: Instant) : TimeProvider {
    override fun now(): Instant = fixedNow
}