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
) : TripPlanApplicationRepository {

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

    override fun findById(id: UUID): TripPlanApplication? {
        return transactionManager.executeRead {
            try {
                entityManager.createQuery(
                    "SELECT tpa FROM TripPlanApplicationJPAEntity tpa WHERE tpa.id = :id",
                    TripPlanApplicationJPAEntity::class.java
                ).setParameter("id", id).singleResult.toDomain()
            } catch (e: NoResultException) {
                null
            }
        }
    }

    override fun findAllByPassengerIdAndTripApplicationStatus(
        passengerId: UUID, tripApplicationStatus: TripPlanApplication.TripApplication.Status?
    ): List<TripPlanApplication> {
        return transactionManager.executeRead {
            try {
                val query = entityManager.createQuery(
                    """
                    |SELECT tpa 
                    |FROM TripPlanApplicationJPAEntity tpa
                    |JOIN tpa.tripApplications ta
                    |WHERE ta.passengerId = :passengerId
                    | ${
                        if (!tripApplicationStatus?.toString()
                                .isNullOrBlank()
                        ) "AND ta.status = :status" else ""
                    }
                    """.trimMargin(), TripPlanApplicationJPAEntity::class.java
                ).setParameter("passengerId", passengerId.toString())

                if (!tripApplicationStatus?.toString().isNullOrBlank()) {
                    query.setParameter("status", tripApplicationStatus.toString())
                }
                query.resultList.map { it.toDomain() }
            } catch (e: NoResultException) {
                emptyList()
            }
        }
    }

    // TODO This method must be written in TripApplicationRepository
    override fun findBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication> {
        return transactionManager.executeRead {
            entityManager.createQuery(
                """
                |SELECT ta FROM TripApplicationJPAEntity ta
                |JOIN ta.sections s
                |WHERE s.id = :sectionId
                |""".trimMargin(), TripApplicationJPAEntity::class.java
            ).setParameter("sectionId", sectionId).resultList.map { it.toDomain() }.toSet()
        }
    }

}
