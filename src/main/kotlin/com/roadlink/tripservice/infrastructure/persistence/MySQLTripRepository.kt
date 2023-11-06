package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import jakarta.persistence.*
import java.util.*

class MySQLTripRepository(
    private val entityManager: EntityManager
) : TripRepository {
    override fun save(trip: Trip) {
        entityManager.persist(TripJPAEntity.from(trip))
    }

    override fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAllByDriverId(driverId: UUID): List<Trip> {
        return entityManager.createQuery(
            """
                |SELECT t 
                |FROM TripJPAEntity t
                |WHERE t.driver = :driverId
                |""".trimMargin(),
            TripJPAEntity::class.java
        )
            .setParameter("driverId", driverId.toString())
            .resultList
            .map { it.toDomain() }
    }

}

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
                driver = trip.driver,
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
            driver = driver,
            vehicle = vehicle,
            departure = departure.toDomain(),
            arrival = arrival.toDomain(),
            meetingPoints = meetingPoints.map { it.toDomain() },
            status = Trip.Status.valueOf(status),
            availableSeats = availableSeats,
        )
    }
}
