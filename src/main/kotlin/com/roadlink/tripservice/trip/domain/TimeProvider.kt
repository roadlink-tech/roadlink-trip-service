package com.roadlink.tripservice.trip.domain

import java.time.Instant

interface TimeProvider {
    fun now(): Instant
}