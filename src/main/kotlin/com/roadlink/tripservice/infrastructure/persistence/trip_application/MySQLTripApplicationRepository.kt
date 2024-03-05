package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import org.hibernate.Session
import java.util.*

class MySQLTripApplicationRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripApplicationRepository {
    override fun saveAll(tripApplications: List<TripPlanApplication.TripApplication>) {
        for (tripApplication in tripApplications) {
            entityManager.persist(TripApplicationJPAEntity.from(tripApplication))
        }
    }

    override fun findAllByDriverId(driverId: UUID): List<TripPlanApplication.TripApplication> {
        return transactionManager.executeRead {
            entityManager.createQuery(
                """
                |SELECT ta 
                |FROM TripApplicationJPAEntity ta
                |JOIN ta.sections s
                |WHERE s.driverId = :driverId
                |""".trimMargin(),
                TripApplicationJPAEntity::class.java
            )
                .setParameter("driverId", driverId.toString())
                .resultList
                .map { it.toDomain() }
        }
    }

    override fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication> {
        return transactionManager.executeRead {
            entityManager.createQuery(
                """
                |SELECT ta 
                |FROM TripApplicationJPAEntity ta
                |JOIN ta.sections s
                |WHERE s.tripId = :tripId
                |""".trimMargin(),
                TripApplicationJPAEntity::class.java
            )
                .setParameter("tripId", tripId)
                .resultList
                .map { it.toDomain() }
        }
    }
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