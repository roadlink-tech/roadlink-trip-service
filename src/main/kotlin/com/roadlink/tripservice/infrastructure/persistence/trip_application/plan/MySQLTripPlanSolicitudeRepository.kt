package com.roadlink.tripservice.infrastructure.persistence.trip_application.plan

import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.TripApplicationJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import java.util.*


class MySQLTripPlanSolicitudeRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripPlanSolicitudeRepository {

    override fun insert(tripPlanSolicitude: TripPlanSolicitude) {
        transactionManager.executeWrite {
            entityManager.persist(TripPlanApplicationJPAEntity.from(tripPlanSolicitude))
        }
    }

    override fun update(tripPlanSolicitude: TripPlanSolicitude) {
        transactionManager.executeWrite {
            entityManager.merge(TripPlanApplicationJPAEntity.from(tripPlanSolicitude))
        }
    }

    override fun find(commandQuery: TripPlanSolicitudeRepository.CommandQuery): List<TripPlanSolicitude> {
        val cq = TripPlanApplicationCommandQuery.from(commandQuery)
        return transactionManager.executeRead {
            val cb = entityManager.criteriaBuilder
            val criteriaQuery = cb.createQuery(TripPlanApplicationJPAEntity::class.java)
            val root = criteriaQuery.from(TripPlanApplicationJPAEntity::class.java)

            val predicates = mutableListOf<Predicate>()

            if (cq.ids.isNotEmpty()) {
                val idPredicate = root.get<UUID>("id").`in`(cq.ids)
                predicates.add(idPredicate)
            }

            if (cq.passengerId != null) {
                val tripApplicationsJoin =
                    root.join<TripPlanApplicationJPAEntity, TripApplicationJPAEntity>("tripApplications")
                val passengerIdPredicate =
                    cb.equal(tripApplicationsJoin.get<String>("passengerId"), cq.passengerId.toString())
                predicates.add(passengerIdPredicate)
            }

            if (cq.tripApplicationId != null) {
                val tripApplicationsJoin = root.join<TripPlanApplicationJPAEntity, TripApplicationJPAEntity>("tripApplications")
                val tripApplicationIdPredicate = cb.equal(tripApplicationsJoin.get<UUID>("id"), cq.tripApplicationId)
                predicates.add(tripApplicationIdPredicate)
            }

            criteriaQuery.select(root).where(cb.and(*predicates.toTypedArray()))

            entityManager.createQuery(criteriaQuery).resultList.map { it.toDomain() }
        }

    }

    data class TripPlanApplicationCommandQuery(
        val ids: List<UUID> = emptyList(),
        val tripApplicationId: UUID? = null,
        val passengerId: UUID? = null,
    ) {
        init {
            require(ids.isNotEmpty() || tripApplicationId != null || passengerId != null) {
                "At least one field must be not null or empty"
            }
        }

        companion object {

            fun from(domainCommandQuery: TripPlanSolicitudeRepository.CommandQuery): TripPlanApplicationCommandQuery {
                return TripPlanApplicationCommandQuery(
                    ids = domainCommandQuery.ids,
                    tripApplicationId = domainCommandQuery.tripApplicationId,
                    passengerId = domainCommandQuery.passengerId
                )
            }
        }
    }
}
