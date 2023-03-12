package com.roadlink.tripservice.domain.time

import java.time.Instant

interface TimeProvider {
    fun now(): Instant
}