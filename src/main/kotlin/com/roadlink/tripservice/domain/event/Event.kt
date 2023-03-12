package com.roadlink.tripservice.domain.event

import java.time.Instant

// TODO use generic here!
sealed class Event {
    abstract val at: Instant
}
