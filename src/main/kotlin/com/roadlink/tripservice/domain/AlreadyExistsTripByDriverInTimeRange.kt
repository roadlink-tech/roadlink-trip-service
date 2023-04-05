package com.roadlink.tripservice.domain

import com.roadlink.tripservice.domain.time.TimeRange

class AlreadyExistsTripByDriverInTimeRange(val driver: String, val timeRange: TimeRange) :
    RuntimeException("Already exists trip by driver '$driver' in time range '$timeRange'")
