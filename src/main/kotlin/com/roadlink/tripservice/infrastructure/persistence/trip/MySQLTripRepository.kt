package com.roadlink.tripservice.infrastructure.persistence.trip

import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import org.hibernate.Session
import java.util.*

class MySQLTripRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripRepository {
    override fun save(trip: Trip) {
        transactionManager.executeWrite {
            entityManager.persist(TripJPAEntity.from(trip))
        }
    }

    override fun existsByDriverAndInTimeRange(driver: String, timeRange: TimeRange): Boolean {
        return transactionManager.executeRead {
            entityManager.createQuery(
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
    }

    override fun findAllByDriverId(driverId: UUID): List<Trip> {
        return transactionManager.executeRead {
            entityManager.createQuery(
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

}
