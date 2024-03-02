package com.roadlink.tripservice.domain.trip.exception

import com.roadlink.tripservice.domain.common.utils.time.TimeRange

class AlreadyExistsTripByDriverInTimeRange(val driver: String, val timeRange: TimeRange) :
    RuntimeException("Already exists trip by driver '$driver' in time range '$timeRange'")
