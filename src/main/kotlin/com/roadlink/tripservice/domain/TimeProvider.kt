package com.roadlink.tripservice.domain

import java.time.Instant

interface TimeProvider {
    fun now(): Instant
}