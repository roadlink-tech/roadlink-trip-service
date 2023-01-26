package com.roadlink.tripservice.dev_tools.domain

import com.roadlink.tripservice.trip.domain.Location

data class GeoapifyPoint(
    val location: Location,
    val formatted: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
)