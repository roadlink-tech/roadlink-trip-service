package com.roadlink.tripservice.infrastructure.rest.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.TripPoint
import java.time.Instant

data class TripPointRequest(
    @JsonProperty(value = "location") val location: LocationRequest,
    @JsonProperty(value = "estimated_arrival_time") val estimatedArrivalTime: String,
    @JsonProperty(value = "address") val address: AddressRequest,
) {
    fun toModel(): TripPoint {
        return TripPoint(
            location = Location(
                latitude = location.latitude,
                longitude = location.longitude
            ),
            at = Instant.parse(estimatedArrivalTime),
            formatted = address.toString(),
            street = address.street,
            city = address.city,
            country = address.country,
            housenumber = address.houseNumber
        )
    }
}