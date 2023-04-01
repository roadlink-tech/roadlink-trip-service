package com.roadlink.tripservice.infrastructure.rest.responses

data class TripPointResponse(
    val location: LocationResponse,
    val at: Long,
    val formatted: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
)