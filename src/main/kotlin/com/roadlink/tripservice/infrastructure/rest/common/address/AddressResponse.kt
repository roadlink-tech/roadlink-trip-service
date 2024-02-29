package com.roadlink.tripservice.infrastructure.rest.common.address

data class AddressResponse(
    val location: LocationResponse,
    val fullAddress: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
)

data class LocationResponse(val latitude: Double, val longitude: Double)