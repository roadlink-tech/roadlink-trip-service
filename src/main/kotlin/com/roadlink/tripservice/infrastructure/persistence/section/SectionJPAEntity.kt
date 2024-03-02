package com.roadlink.tripservice.infrastructure.persistence.section

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.common.address.Address
import com.roadlink.tripservice.domain.common.TripPoint
import com.roadlink.tripservice.domain.trip.section.Section
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.util.*

@Entity
@Table(name = "section")
data class SectionJPAEntity(
    @Id val id: String,
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "tripId", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    val tripId: UUID,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "estimatedArrivalTime", column = Column(name = "departure_estimated_arrival_time")),
        AttributeOverride(name = "fullAddress", column = Column(name = "departure_full_address")),
        AttributeOverride(name = "street", column = Column(name = "departure_street")),
        AttributeOverride(name = "city", column = Column(name = "departure_city")),
        AttributeOverride(name = "country", column = Column(name = "departure_country")),
        AttributeOverride(name = "housenumber", column = Column(name = "departure_housenumber")),
        AttributeOverride(name = "latitude", column = Column(name = "departure_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "departure_longitude"))
    )
    val departure: TripPointJPAEntity,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "estimatedArrivalTime", column = Column(name = "arrival_estimated_arrival_time")),
        AttributeOverride(name = "fullAddress", column = Column(name = "arrival_full_address")),
        AttributeOverride(name = "street", column = Column(name = "arrival_street")),
        AttributeOverride(name = "city", column = Column(name = "arrival_city")),
        AttributeOverride(name = "country", column = Column(name = "arrival_country")),
        AttributeOverride(name = "housenumber", column = Column(name = "arrival_housenumber")),
        AttributeOverride(name = "latitude", column = Column(name = "arrival_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "arrival_longitude"))
    )
    val arrival: TripPointJPAEntity,

    val distanceInMeters: Double,
    // TODO esto es el driverId?
    val driver: String,
    val vehicle: String,
    var initialAmountOfSeats: Int,
    var bookedSeats: Int
) {
    companion object {
        fun from(section: Section): SectionJPAEntity {
            return SectionJPAEntity(
                id = section.id,
                tripId = section.tripId,
                departure = TripPointJPAEntity.from(section.departure),
                arrival = TripPointJPAEntity.from(section.arrival),
                distanceInMeters = section.distanceInMeters,
                driver = section.driverId,
                vehicle = section.vehicleId,
                initialAmountOfSeats = section.initialAmountOfSeats,
                bookedSeats = section.bookedSeats
            )
        }
    }

    fun toDomain(): Section =
        Section(
            id = id,
            tripId = tripId,
            departure = departure.toDomain(),
            arrival = arrival.toDomain(),
            distanceInMeters = distanceInMeters,
            driverId = driver,
            vehicleId = vehicle,
            initialAmountOfSeats = initialAmountOfSeats,
            bookedSeats = bookedSeats,
        )
}

@Embeddable
data class TripPointJPAEntity(
    val estimatedArrivalTime: Instant,
    val latitude: Double,
    val longitude: Double,
    val fullAddress: String,
    val street: String,
    val city: String,
    val country: String,
    val housenumber: String,
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
                housenumber = trip.address.houseNumber
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
                houseNumber = housenumber,
            ),
        )
}