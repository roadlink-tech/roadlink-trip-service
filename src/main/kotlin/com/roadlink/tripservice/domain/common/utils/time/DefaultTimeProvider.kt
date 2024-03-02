package com.roadlink.tripservice.domain.common.utils.time

import java.time.Instant

class DefaultTimeProvider : TimeProvider {
    override fun now(): Instant {
        return Instant.now()
    }
}