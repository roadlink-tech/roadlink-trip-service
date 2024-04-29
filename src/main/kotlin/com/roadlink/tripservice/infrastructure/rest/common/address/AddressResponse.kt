package com.roadlink.tripservice.infrastructure.rest.common.address

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.common.address.Address

data class AddressResponse(
    val location: LocationResponse,
    val fullAddress: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
) {
    companion object {
        fun from(address: Address): AddressResponse {
            return AddressResponse(
                location = LocationResponse.from(address.location),
                fullAddress = address.fullAddress,
                street = address.street,
                city = address.city,
                country = address.country,
                housenumber = address.houseNumber
            )
        }
    }
}

data class LocationResponse(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun from(location: Location): LocationResponse {
            return LocationResponse(
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }
}