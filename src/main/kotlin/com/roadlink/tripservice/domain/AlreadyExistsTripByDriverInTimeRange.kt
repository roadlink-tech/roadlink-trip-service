package com.roadlink.tripservice.domain

class AlreadyExistsTripByDriverInTimeRange(val driver: String, val timeRange: TimeRange)
    : RuntimeException("Already exists trip by driver '$driver' in time range '$timeRange'")
