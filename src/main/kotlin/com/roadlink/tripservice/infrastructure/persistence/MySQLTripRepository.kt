package com.roadlink.tripservice.infrastructure.persistence

import com.roadlink.tripservice.domain.time.TimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import jakarta.persistence.EntityManager
import java.util.*

class MySQLTripRepository(
    private val entityManager: EntityManager
) : TripRepository {
    override fun save(trip: Trip) {
        entityManager.persist(TripJPAEntity.from(trip))
    }

    override fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean {
        return entityManager.createQuery(
        """
            |SELECT t 
            |FROM TripJPAEntity t
            |WHERE 
            |   t.driver = :driverId
            |   AND NOT :to <= t.departure.estimatedArrivalTime
            |   AND NOT :from >= t.arrival.estimatedArrivalTime
            |""".trimMargin(),
        TripJPAEntity::class.java
    )
        .setParameter("driverId", driver)
        .setParameter("to", timeRange.to)
        .setParameter("from", timeRange.from)
        .resultList
        .map { it.toDomain() }
        .isNotEmpty()
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
