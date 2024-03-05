package com.roadlink.tripservice.infrastructure.persistence.common

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.common.address.Address
import jakarta.persistence.Embeddable
import java.time.Instant

@Embeddable
data class TripPointJPAEntity(
    val estimatedArrivalTime: Instant,
    val latitude: Double,
    val longitude: Double,
    val fullAddress: String,
    val street: String,
    val city: String,
    val country: String,
    val houseNumber: String,
) {
    companion object {
        fun from(trip: TripPoint): TripPointJPAEntity {
            return TripPointJPAEntity(
                estimatedArrivalTime = trip.estimatedArrivalTime,
                latitude = trip.address.location.latitude,
                longitude = trip.address.location.longitude,
                fullAddress = trip.address.fullAddress,
                city = trip.address.city,
                country = trip.address.country,
                street = trip.address.street,
                houseNumber = trip.address.houseNumber
            )
        }
    }

    fun toDomain(): TripPoint =
        TripPoint(
            estimatedArrivalTime = estimatedArrivalTime,
            address = Address(
                location = Location(
                    latitude = latitude,
                    longitude = longitude,
                ),
                fullAddress = fullAddress,
                street = street,
                city = city,
                country = country,
                houseNumber = houseNumber,
            ),
        )
}