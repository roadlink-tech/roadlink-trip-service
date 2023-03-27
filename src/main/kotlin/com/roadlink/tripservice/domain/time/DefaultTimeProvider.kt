package com.roadlink.tripservice.domain.time

import java.time.Instant

class DefaultTimeProvider : TimeProvider {
    override fun now(): Instant {
        return Instant.now()
    }
}