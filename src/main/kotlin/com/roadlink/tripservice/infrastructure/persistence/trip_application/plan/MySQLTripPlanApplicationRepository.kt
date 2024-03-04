package com.roadlink.tripservice.infrastructure.persistence.trip_application.plan

import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.infrastructure.persistence.trip_application.TripApplicationJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import org.hibernate.Session
import java.util.*


class MySQLTripPlanApplicationRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
): TripPlanApplicationRepository {

    override fun insert(application: TripPlanApplication) {
        transactionManager.executeWrite {
            entityManager.persist(TripPlanApplicationJPAEntity.from(application))
        }
    }

    override fun update(application: TripPlanApplication) {
        transactionManager.executeWrite {
            entityManager.merge(TripPlanApplicationJPAEntity.from(application))
        }
    }

    override fun findByTripApplicationId(tripApplicationId: UUID): TripPlanApplication? {
        return transactionManager.executeRead {
            try {
                entityManager.createQuery(
                    "SELECT tpa FROM TripPlanApplicationJPAEntity tpa WHERE tpa.id = :id",
                    TripPlanApplicationJPAEntity::class.java
                )
                    .setParameter("id", tripApplicationId)
                    .singleResult
                    .toDomain()
            } catch (e: NoResultException) {
                null
            }
        }
    }

    override fun findBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication> {
        return transactionManager.executeRead {
            entityManager.createQuery(
                """
                |SELECT ta FROM TripApplicationJPAEntity ta
                |JOIN ta.sections s
                |WHERE s.id = :sectionId
                |""".trimMargin(),
                TripApplicationJPAEntity::class.java
            )
                .setParameter("sectionId", sectionId)
                .resultList
                .map { it.toDomain() }
                .toSet()
        }
    }

}
