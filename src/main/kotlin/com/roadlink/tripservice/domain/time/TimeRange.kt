package com.roadlink.tripservice.domain.time

import com.roadlink.tripservice.domain.time.exception.TimeRangeToIsBeforeFromException
import java.time.Instant

data class TimeRange(val from: Instant, val to: Instant) {

    init {
        if (to.isBefore(from))
            throw TimeRangeToIsBeforeFromException(from = from, to = to)
    }

    fun intersects(other: TimeRange): Boolean =
        !other.to.isBefore(this.from) && !other.from.isAfter(this.to)
}