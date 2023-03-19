package com.roadlink.tripservice.domain.event

import java.time.Instant
sealed class Event{
    abstract val at: Instant
}

