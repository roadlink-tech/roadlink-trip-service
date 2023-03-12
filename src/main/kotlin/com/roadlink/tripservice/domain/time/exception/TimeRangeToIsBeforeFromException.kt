package com.roadlink.tripservice.domain.time.exception

import java.time.Instant

class TimeRangeToIsBeforeFromException(val from: Instant, val to: Instant)
    : RuntimeException("Time range to '$to' is before from '$from'")
