package com.roadlink.tripservice.trip.infrastructure.rest.responses

data class TripPointExpectedResponse(
    val location: LocationExpectedResponse,
    val at: Long,
    val formatted: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
)
