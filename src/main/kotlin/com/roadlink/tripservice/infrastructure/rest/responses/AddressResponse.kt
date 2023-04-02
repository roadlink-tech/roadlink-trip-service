package com.roadlink.tripservice.infrastructure.rest.responses

data class AddressResponse(
    val location: LocationResponse,
    val fullAddress: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
)
