package com.roadlink.tripservice.infrastructure.persistence.trip_plan

import com.roadlink.tripservice.domain.trip_plan.TripPlan
import com.roadlink.tripservice.domain.trip_plan.TripPlanRepository
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import java.util.*

class MySQLTripPlanRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripPlanRepository {
    override fun insert(tripPlan: TripPlan): TripPlan {
        transactionManager.executeWrite {
            entityManager.persist(TripPlanJPAEntity.from(tripPlan))
        }
        return tripPlan
    }

    override fun update(tripPlan: TripPlan): TripPlan {
        transactionManager.executeWrite {
            entityManager.merge(TripPlanJPAEntity.from(tripPlan))
        }
        return tripPlan
    }

    override fun find(commandQuery: TripPlanRepository.CommandQuery): List<TripPlan> {
        val cq = TripPlanCommandQuery.from(commandQuery)
        return transactionManager.executeRead {
            val cb = entityManager.criteriaBuilder
            val criteriaQuery = cb.createQuery(TripPlanJPAEntity::class.java)
            val root = criteriaQuery.from(TripPlanJPAEntity::class.java)

            val predicates = mutableListOf<Predicate>()

            if (cq.ids.isNotEmpty()) {
                val idPredicate = root.get<UUID>("id").`in`(cq.ids)
                predicates.add(idPredicate)
            }

            if (cq.passengerId.isNotEmpty()) {
                val idPredicate = root.get<UUID>("passengerId").`in`(cq.passengerId)
                predicates.add(idPredicate)
            }

            criteriaQuery.select(root).where(cb.and(*predicates.toTypedArray()))

            entityManager.createQuery(criteriaQuery).resultList.map { it.toDomain() }
        }
    }

    data class TripPlanCommandQuery(
        val ids: List<UUID?> = emptyList(),
        val passengerId: List<UUID?> = emptyList()
    ) {
        init {
            require(ids.isNotEmpty() || passengerId.isNotEmpty()) {
                "At least one field must be not null or empty"
            }
        }

        companion object {

            fun from(domainCommandQuery: TripPlanRepository.CommandQuery): TripPlanCommandQuery {
                return TripPlanCommandQuery(
                    ids = listOfNotNull(domainCommandQuery.id),
                    passengerId = listOfNotNull(domainCommandQuery.passengerId)
                )
            }
        }
    }
}