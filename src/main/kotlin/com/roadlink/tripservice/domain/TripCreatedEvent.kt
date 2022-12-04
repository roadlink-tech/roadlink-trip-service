package com.roadlink.tripservice.domain

import java.time.Instant

data class TripCreatedEvent(val trip: Trip, override val at: Instant) : Event()
