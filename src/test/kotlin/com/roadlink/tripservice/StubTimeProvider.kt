package com.roadlink.tripservice

import com.roadlink.tripservice.domain.TimeProvider
import java.time.Instant

class StubTimeProvider(private val fixedNow: Instant) : TimeProvider {
    override fun now(): Instant = fixedNow
}