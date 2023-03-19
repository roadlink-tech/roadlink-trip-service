package com.roadlink.tripservice.domain.event

import com.roadlink.tripservice.domain.trip.Trip
import java.time.Instant

// TODO este evento debería estar en trip pero no lo podemos mover porque es una sealed class
data class TripCreatedEvent(val trip: Trip, override val at: Instant) : Event()

data class TripCreatedCommandV2(val trip: Trip, val at: Instant) : Command

data class TripCreatedCommandResponseV2(val trip: Trip) : CommandResponse
