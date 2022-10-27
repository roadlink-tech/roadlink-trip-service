package com.roadlink.tripservice.domain

import java.time.Instant

data class TripPoint(val location: Location, val at: Instant)
