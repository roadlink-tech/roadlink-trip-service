package com.roadlink.tripservice.infrastructure.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.Location
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripPoint
import com.roadlink.tripservice.usecases.CreateTrip
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import java.time.Instant


@Controller("/trip-service/trip")
class CreateTripController(private val createTrip: CreateTrip) {

    @Post
    fun createTrip(@Body request: CreateTripRequest): HttpResponse<Trip> {
        val trip = createTrip.invoke(request.toDomain())
        return HttpResponse.status<Trip?>(HttpStatus.CREATED).body(trip)
    }
}

data class LocationRequested(
    @JsonProperty(value = "latitude") val latitude: Double,
    @JsonProperty(value = "longitude") val longitude: Double
)

data class AddressRequested(
    @JsonProperty(value = "street") val street: String,
    @JsonProperty(value = "city") val city: String,
    @JsonProperty(value = "country") val country: String,
    @JsonProperty(value = "house_number") val houseNumber: String
) {
    override fun toString(): String {
        return "$street, $city, $country, $houseNumber"
    }
}

data class TripPointRequested(
    @JsonProperty(value = "location") val location: LocationRequested,
    @JsonProperty(value = "estimated_arrival_time") val estimatedArrivalTime: String,
    @JsonProperty(value = "address") val address: AddressRequested,
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

data class CreateTripRequest(
    @JsonProperty(value = "driver") val driver: String,
    @JsonProperty(value = "vehicle") val vehicle: String,
    @JsonProperty(value = "departure") val departure: TripPointRequested,
    @JsonProperty(value = "arrival") val arrival: TripPointRequested,
    @JsonProperty(value = "meeting_points") val meetingPoints: List<TripPointRequested>,
    @JsonProperty(value = "available_seats") val availableSeats: Int,
) {
    fun toDomain(): CreateTrip.Request {
        return CreateTrip.Request(
            driver = this.driver,
            vehicle = this.vehicle,
            departure = this.departure.toModel(),
            arrival = this.arrival.toModel(),
            meetingPoints = meetingPoints.map { it.toModel() },
            availableSeats = availableSeats
        )
    }
}