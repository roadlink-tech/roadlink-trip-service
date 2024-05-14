package com.roadlink.tripservice.infrastructure.rest.trip.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.common.address.Address
import com.roadlink.tripservice.domain.trip.constraint.Policy
import com.roadlink.tripservice.domain.trip.constraint.Preferences
import com.roadlink.tripservice.domain.trip.constraint.Restriction
import com.roadlink.tripservice.domain.trip.constraint.Rule
import com.roadlink.tripservice.domain.trip.constraint.Visibility
import com.roadlink.tripservice.usecases.trip.CreateTrip
import java.time.Instant

data class CreateTripRequest(
    @JsonProperty(value = "driver") val driver: String,
    @JsonProperty(value = "vehicle") val vehicle: String,
    @JsonProperty(value = "departure") val departure: TripPointRequest,
    @JsonProperty(value = "arrival") val arrival: TripPointRequest,
    @JsonProperty(value = "meeting_points") @JsonInclude(JsonInclude.Include.ALWAYS) val meetingPoints: List<TripPointRequest>,
    @JsonProperty(value = "policies") @JsonInclude(JsonInclude.Include.ALWAYS) val policies: List<PolicyTypeRequest> = emptyList(),
    @JsonProperty(value = "restrictions") @JsonInclude(JsonInclude.Include.ALWAYS) val restrictions: List<RestrictionTypeRequest> = emptyList(),
    @JsonProperty(value = "available_seats") val availableSeats: Int,
) {
    fun toDomain(): CreateTrip.Input {
        return CreateTrip.Input(
            driver = this.driver,
            vehicle = this.vehicle,
            departure = this.departure.toModel(),
            arrival = this.arrival.toModel(),
            meetingPoints = meetingPoints.map { it.toModel() },
            availableSeats = availableSeats,
            policies = policies.map { it.toDomain() },
            restrictions = restrictions.map { it.toDomain() }
        )
    }

    enum class PolicyTypeRequest {
        NO_SMOKING {
            override fun toDomain(): Policy {
                return Rule.NoSmoking
            }
        },
        PET_ALLOWED {
            override fun toDomain(): Policy {
                return Rule.PetAllowed
            }
        },
        UPCOMING_YEAR {
            override fun toDomain(): Policy {
                return Preferences.UpcomingYear
            }
        };

        abstract fun toDomain(): Policy
    }

    enum class RestrictionTypeRequest {
        ONLY_FRIENDS {
            override fun toDomain(): Restriction {
                return Visibility.OnlyFriends
            }
        },
        ONLY_WOMEN {
            override fun toDomain(): Restriction {
                return Visibility.OnlyWomen
            }
        };

        abstract fun toDomain(): Restriction
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