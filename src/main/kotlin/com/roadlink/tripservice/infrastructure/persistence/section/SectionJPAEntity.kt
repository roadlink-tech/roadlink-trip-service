package com.roadlink.tripservice.infrastructure.persistence.section

import com.roadlink.tripservice.domain.trip.section.Section
import com.roadlink.tripservice.infrastructure.persistence.common.Jts
import com.roadlink.tripservice.infrastructure.persistence.common.TripPointJPAEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Point
import java.util.*

@Entity
@Table(
    name = "section", indexes = [
        Index(name = "section_trip_id_idx", columnList = "trip_id"),
        Index(name = "section_departure_point_idx", columnList = "departure_point")
    ]
)
data class SectionJPAEntity(
    @Id val id: String,
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "trip_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    val tripId: UUID,
// TODO tiene que haber un indice por tripID
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
    @Column(nullable = false, columnDefinition = "GEOMETRY SRID 4326")
    val departurePoint: Point,

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
    @Column(name = "distance_in_meters")
    val distanceInMeters: Double,
    @Column(name = "driver_id")
    val driverId: String,
    @Column(name = "vehicle_id")
    val vehicleId: String,
    @Column(name = "initial_amount_of_seats")
    var initialAmountOfSeats: Int,
    @Column(name = "booked_seats")
    var bookedSeats: Int
) {
    companion object {
        fun from(section: Section): SectionJPAEntity {
            val departureCoordinate = Coordinate(
                section.departure.address.location.latitude,
                section.departure.address.location.longitude
            )

            return SectionJPAEntity(
                id = section.id,
                tripId = section.tripId,
                departure = TripPointJPAEntity.from(section.departure),
                departurePoint = Jts.geometryFactory.createPoint(departureCoordinate),
                arrival = TripPointJPAEntity.from(section.arrival),
                distanceInMeters = section.distanceInMeters,
                driverId = section.driverId,
                vehicleId = section.vehicleId,
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
            driverId = driverId,
            vehicleId = vehicleId,
            initialAmountOfSeats = initialAmountOfSeats,
            bookedSeats = bookedSeats,
        )
}

