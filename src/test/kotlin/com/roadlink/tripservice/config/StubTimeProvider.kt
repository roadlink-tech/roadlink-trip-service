package com.roadlink.tripservice.config

import com.roadlink.tripservice.domain.common.utils.time.TimeProvider
import java.time.Instant

class StubTimeProvider(private val fixedNow: Instant) : TimeProvider {
    override fun now(): Instant = fixedNow
}