package com.roadlink.tripservice.trip.domain

import java.time.Instant

data class TripCreatedEvent(val trip: Trip, override val at: Instant) : Event()
