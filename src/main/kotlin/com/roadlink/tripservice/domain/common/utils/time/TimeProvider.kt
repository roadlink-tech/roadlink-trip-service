package com.roadlink.tripservice.domain.common.utils.time

import java.time.Instant

interface TimeProvider {
    fun now(): Instant
}