package com.roadlink.tripservice.domain.trip

import com.roadlink.tripservice.domain.Location
import java.time.Instant

data class TripPoint(
    val location: Location,
    // TODO the estimated arrival time
    val at: Instant,
    // TODO address formatted or full address?
    val formatted: String,
    // TODO all this attributes might be moved to an Address domain entity
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
)
