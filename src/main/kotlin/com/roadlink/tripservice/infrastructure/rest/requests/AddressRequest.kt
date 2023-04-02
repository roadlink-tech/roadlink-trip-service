package com.roadlink.tripservice.infrastructure.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.Address

data class AddressRequest(
    @JsonProperty(value = "location") val location: LocationRequest,
    @JsonProperty(value = "street") val street: String,
    @JsonProperty(value = "city") val city: String,
    @JsonProperty(value = "country") val country: String,
    @JsonProperty(value = "house_number") val houseNumber: String
) {
    override fun toString(): String {
        return "$street $houseNumber, $city"
    }

    fun toDomain() =
        Address(
            location = Location(
                latitude = location.latitude,
                longitude = location.longitude
            ),
            fullAddress = toString(),
            street = street,
            city = city,
            country = country,
            housenumber = houseNumber
        )
}