package com.roadlink.tripservice.domain.trip.events.commands

import com.roadlink.tripservice.domain.trip.Trip
import java.time.Instant

data class TripCreatedCommand(val trip: Trip, val at: Instant) : Command
