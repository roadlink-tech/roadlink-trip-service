package com.roadlink.tripservice.infrastructure.persistence.trip

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.infrastructure.persistence.section.TripPointJPAEntity
import jakarta.persistence.*

@Entity
@Table(name = "trip")
data class TripJPAEntity(
    @Id val id: String,
    val driver: String,
    val vehicle: String,
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
    @ElementCollection
    val meetingPoints: List<TripPointJPAEntity>,
    val status: String,
    val availableSeats: Int,
) {
    companion object {
        fun from(trip: Trip): TripJPAEntity {
            return TripJPAEntity(
                id = trip.id,
                driver = trip.driverId,
                vehicle = trip.vehicle,
                departure = TripPointJPAEntity.from(trip.departure),
                arrival = TripPointJPAEntity.from(trip.arrival),
                meetingPoints = trip.meetingPoints.map { TripPointJPAEntity.from(it) },
                status = trip.status.name,
                availableSeats = trip.availableSeats,
            )
        }
    }

    fun toDomain(): Trip {
        return Trip(
            id = id,
            driverId = driver,
            vehicle = vehicle,
            departure = departure.toDomain(),
            arrival = arrival.toDomain(),
            meetingPoints = meetingPoints.map { it.toDomain() },
            status = Trip.Status.valueOf(status),
            availableSeats = availableSeats,
        )
    }
}