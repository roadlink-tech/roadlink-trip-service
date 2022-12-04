package com.roadlink.tripservice.domain

import java.time.Instant

data class TimeRange(val from: Instant, val to: Instant) {

    init {
        if (to.isBefore(from))
            throw TimeRangeToIsBeforeFrom(from = from, to = to)
    }

    fun intersects(other: TimeRange): Boolean =
        !other.to.isBefore(this.from) && !other.from.isAfter(this.to)
}