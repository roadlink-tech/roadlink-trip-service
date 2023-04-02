package com.roadlink.tripservice.trip.infrastructure.rest.responses

data class AddressExpectedResponse(
    val location: LocationExpectedResponse,
    val fullAddress: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
)
