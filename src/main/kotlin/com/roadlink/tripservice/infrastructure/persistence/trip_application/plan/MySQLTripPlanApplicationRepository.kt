package com.roadlink.tripservice.infrastructure.persistence.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.section.SectionJPAEntity
import com.roadlink.tripservice.infrastructure.persistence.trip_application.MySQLTripApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.TripApplicationJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import java.util.*


class MySQLTripPlanApplicationRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripPlanApplicationRepository {

    override fun insert(tripPlanApplication: TripPlanApplication) {
        transactionManager.executeWrite {
            entityManager.persist(TripPlanApplicationJPAEntity.from(tripPlanApplication))
        }
    }

    override fun update(tripPlanApplication: TripPlanApplication) {
        transactionManager.executeWrite {
            entityManager.merge(TripPlanApplicationJPAEntity.from(tripPlanApplication))
        }
    }

    override fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication? {
        return transactionManager.executeRead {
            try {
                entityManager.createQuery(
                    """
                    |SELECT tpa 
                    |FROM TripPlanApplicationJPAEntity tpa 
                    |JOIN tpa.tripApplications ta
                    |WHERE ta.id = :id
                    """.trimMargin(), TripPlanApplicationJPAEntity::class.java
                ).setParameter("id", tripApplicationId).singleResult.toDomain()
            } catch (e: NoResultException) {
                null
            }
        }
    }

    override fun find(commandQuery: TripPlanApplicationRepository.CommandQuery): List<TripPlanApplication> {
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

            fun from(domainCommandQuery: TripPlanApplicationRepository.CommandQuery): TripPlanApplicationCommandQuery {
                return TripPlanApplicationCommandQuery(
                    ids = domainCommandQuery.ids,
                    tripApplicationId = domainCommandQuery.tripApplicationId,
                    passengerId = domainCommandQuery.passengerId
                )
            }
        }
    }
}
