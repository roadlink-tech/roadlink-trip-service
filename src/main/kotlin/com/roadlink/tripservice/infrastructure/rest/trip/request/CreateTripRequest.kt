package com.roadlink.tripservice.infrastructure.rest.trip.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.Address
import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.usecases.trip.CreateTrip
import java.time.Instant

data class CreateTripRequest(
    @JsonProperty(value = "driver") val driver: String,
    @JsonProperty(value = "vehicle") val vehicle: String,
    @JsonProperty(value = "departure") val departure: TripPointRequest,
    @JsonProperty(value = "arrival") val arrival: TripPointRequest,
    @JsonProperty(value = "meeting_points") val meetingPoints: List<TripPointRequest>,
    @JsonProperty(value = "available_seats") val availableSeats: Int,
) {
    fun toDomain(): CreateTrip.Input {
        return CreateTrip.Input(
            driver = this.driver,
            vehicle = this.vehicle,
            departure = this.departure.toModel(),
            arrival = this.arrival.toModel(),
            meetingPoints = meetingPoints.map { it.toModel() },
            availableSeats = availableSeats
        )
    }
}

data class TripPointRequest(
    @JsonProperty(value = "estimated_arrival_time") val estimatedArrivalTime: String,
    @JsonProperty(value = "address") val address: AddressRequest,
) {
    fun toModel(): TripPoint {
        return TripPoint(
            estimatedArrivalTime = Instant.parse(estimatedArrivalTime),
            address = address.toDomain(),
        )
    }
}

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
            houseNumber = houseNumber
        )
}

data class LocationRequest(
    @JsonProperty(value = "latitude") val latitude: Double,
    @JsonProperty(value = "longitude") val longitude: Double
)