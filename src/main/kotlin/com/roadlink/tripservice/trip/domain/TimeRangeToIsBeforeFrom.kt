package com.roadlink.tripservice.trip.domain

import java.time.Instant

class TimeRangeToIsBeforeFrom(val from: Instant, val to: Instant)
    : RuntimeException("Time range to '$to' is before from '$from'")
