package com.roadlink.tripservice.domain

import java.time.Instant

sealed class Event {
    abstract val at: Instant
}
