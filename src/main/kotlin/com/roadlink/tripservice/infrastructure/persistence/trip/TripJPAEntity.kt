package com.roadlink.tripservice.infrastructure.persistence.trip

import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.infrastructure.persistence.common.TripPointJPAEntity
import com.roadlink.tripservice.infrastructure.persistence.trip.constraint.PolicyJPAEntity
import com.roadlink.tripservice.infrastructure.persistence.trip.constraint.PolicyListConverter
import com.roadlink.tripservice.infrastructure.persistence.trip.constraint.RestrictionJPAEntity
import com.roadlink.tripservice.infrastructure.persistence.trip.constraint.RestrictionListConverter
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "trip")
data class TripJPAEntity(
    @Id val id: String,
    @Column(name = "driver_id")
    val driverId: String,
    @Column(name = "vehicle_id")
    val vehicleId: String,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(
            name = "estimatedArrivalTime",
            column = Column(name = "departure_estimated_arrival_time")
        ),
        AttributeOverride(name = "fullAddress", column = Column(name = "departure_full_address")),
        AttributeOverride(name = "street", column = Column(name = "departure_street")),
        AttributeOverride(name = "city", column = Column(name = "departure_city")),
        AttributeOverride(name = "country", column = Column(name = "departure_country")),
        AttributeOverride(name = "houseNumber", column = Column(name = "departure_house_number")),
        AttributeOverride(name = "latitude", column = Column(name = "departure_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "departure_longitude"))
    )
    val departure: TripPointJPAEntity,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(
            name = "estimatedArrivalTime",
            column = Column(name = "arrival_estimated_arrival_time")
        ),
        AttributeOverride(name = "fullAddress", column = Column(name = "arrival_full_address")),
        AttributeOverride(name = "street", column = Column(name = "arrival_street")),
        AttributeOverride(name = "city", column = Column(name = "arrival_city")),
        AttributeOverride(name = "country", column = Column(name = "arrival_country")),
        AttributeOverride(name = "houseNumber", column = Column(name = "arrival_house_number")),
        AttributeOverride(name = "latitude", column = Column(name = "arrival_latitude")),
        AttributeOverride(name = "longitude", column = Column(name = "arrival_longitude"))
    )
    val arrival: TripPointJPAEntity,
    @ElementCollection
    val meetingPoints: List<TripPointJPAEntity>,
    @Column(name = "status")
    val status: String,
    @Column(name = "available_seats")
    val availableSeats: Int,
    @Convert(converter = PolicyListConverter::class)
    val policies: List<PolicyJPAEntity>,
    @Convert(converter = RestrictionListConverter::class)
    val restrictions: List<RestrictionJPAEntity>,
) {
    companion object {
        fun from(trip: Trip): TripJPAEntity {
            return TripJPAEntity(
                id = trip.id,
                driverId = trip.driverId,
                vehicleId = trip.vehicle,
                departure = TripPointJPAEntity.from(trip.departure),
                arrival = TripPointJPAEntity.from(trip.arrival),
                meetingPoints = trip.meetingPoints.map { TripPointJPAEntity.from(it) },
                status = trip.status.name,
                availableSeats = trip.availableSeats,
                policies = trip.policies.map { PolicyJPAEntity.from(it) },
                restrictions = trip.restrictions.map { RestrictionJPAEntity.from(it) }
            )
        }
    }

    fun toDomain(): Trip {
        return Trip(
            id = id,
            driverId = driverId,
            vehicle = vehicleId,
            departure = departure.toDomain(),
            arrival = arrival.toDomain(),
            meetingPoints = meetingPoints.map { it.toDomain() },
            status = Trip.Status.valueOf(status),
            availableSeats = availableSeats,
            policies = policies.map { it.toDomain() },
            restrictions = restrictions.map { it.toDomain() }
        )
    }
}