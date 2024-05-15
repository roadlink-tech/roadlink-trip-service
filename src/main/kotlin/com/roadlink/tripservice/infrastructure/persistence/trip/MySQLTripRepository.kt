package com.roadlink.tripservice.infrastructure.persistence.trip

import com.roadlink.tripservice.domain.common.utils.time.TimeRange
import com.roadlink.tripservice.domain.trip.Trip
import com.roadlink.tripservice.domain.trip.TripRepository
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import java.util.*

class MySQLTripRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripRepository {
    override fun insert(trip: Trip) {
        transactionManager.executeWrite {
            entityManager.persist(TripJPAEntity.from(trip))
        }
    }

    override fun update(trip: Trip) {
        transactionManager.executeWrite {
            entityManager.merge(TripJPAEntity.from(trip))
        }
    }

    override fun existsByDriverAndInTimeRange(driverId: String, timeRange: TimeRange): Boolean {
        return transactionManager.executeRead {
            entityManager.createQuery(
                """
            |SELECT t 
            |FROM TripJPAEntity t
            |WHERE 
            |   t.driverId = :driverId
            |   AND NOT :to <= t.departure.estimatedArrivalTime
            |   AND NOT :from >= t.arrival.estimatedArrivalTime
            |""".trimMargin(),
                TripJPAEntity::class.java
            )
                .setParameter("driverId", driverId)
                .setParameter("to", timeRange.to)
                .setParameter("from", timeRange.from)
                .resultList
                .map { it.toDomain() }
                .isNotEmpty()
        }
    }

    override fun find(commandQuery: TripRepository.CommandQuery): List<Trip> {
        val cq = TripCommandQuery.from(commandQuery)
        return transactionManager.executeRead {
            val cb = entityManager.criteriaBuilder
            val criteriaQuery = cb.createQuery(TripJPAEntity::class.java)
            val root = criteriaQuery.from(TripJPAEntity::class.java)

            val predicates = mutableListOf<Predicate>()

            if (cq.ids.isNotEmpty()) {
                val idPredicate = root.get<UUID>("id").`in`(cq.ids.map { it.toString() })
                predicates.add(idPredicate)
            }

            if (cq.driverId != null) {
                val driverIdPredicate = cb.equal(root.get<UUID>("driverId"), cq.driverId.toString())
                predicates.add(driverIdPredicate)
            }

            criteriaQuery.select(root).where(cb.and(*predicates.toTypedArray()))
            entityManager.createQuery(criteriaQuery).resultList.map { it.toDomain() }
        }
    }

    data class TripCommandQuery(
        val ids: List<UUID?> = emptyList(),
        val driverId: UUID? = null
    ) {
        init {
            require(ids.isNotEmpty() || driverId != null) {
                "At least one field must be not null or empty"
            }
        }

        companion object {

            fun from(domainCommandQuery: TripRepository.CommandQuery): TripCommandQuery {
                return TripCommandQuery(
                    ids = domainCommandQuery.ids,
                    driverId = domainCommandQuery.driverId
                )
            }
        }
    }

}
