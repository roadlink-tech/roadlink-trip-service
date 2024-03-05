package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.infrastructure.persistence.common.TripPointJPAEntity
import com.roadlink.tripservice.infrastructure.persistence.section.SectionCommandQuery
import com.roadlink.tripservice.infrastructure.persistence.section.SectionJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import java.time.Instant
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
            find(TripApplicationRepository.CommandQuery(driverId = driverId)).toMutableList()
        }
    }

    override fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication> {
        return transactionManager.executeRead {
            find(TripApplicationRepository.CommandQuery(tripId = tripId)).toMutableList()
        }
    }

    override fun findBySectionId(sectionId: String): Set<TripPlanApplication.TripApplication> {
        return transactionManager.executeRead {
            find(TripApplicationRepository.CommandQuery(sectionId)).toMutableSet()
        }
    }

    override fun find(domainCommandQuery: TripApplicationRepository.CommandQuery): List<TripPlanApplication.TripApplication> {
        val commandQuery = TripApplicationCommandQuery.from(domainCommandQuery)
        val cb = entityManager.criteriaBuilder
        val criteriaQuery = cb.createQuery(TripApplicationJPAEntity::class.java)
        val root = criteriaQuery.from(TripApplicationJPAEntity::class.java)

        val predicates = mutableListOf<Predicate>()

        if (commandQuery.sectionId.isNotEmpty()) {
            val sections = root.join<TripApplicationJPAEntity, SectionJPAEntity>("sections")
            val predicate = cb.equal(sections.get<String>("id"), commandQuery.sectionId)
            predicates.add(predicate)
        }

        if (commandQuery.tripId != null) {
            val sections = root.join<TripApplicationJPAEntity, SectionJPAEntity>("sections")
            val tripIdPredicate = cb.equal(sections.get<String>("tripId"), commandQuery.tripId)
            predicates.add(tripIdPredicate)
        }

        if (commandQuery.driverId != null) {
            val sections = root.join<TripApplicationJPAEntity, SectionJPAEntity>("sections")
            val driverIdPredicate = cb.equal(sections.get<String>("driverId"), commandQuery.driverId.toString())
            predicates.add(driverIdPredicate)
        }
        criteriaQuery.select(root).where(cb.and(*predicates.toTypedArray()))

        return entityManager.createQuery(criteriaQuery).resultList.map { it.toDomain() }
    }

    data class TripApplicationCommandQuery(
        val sectionId: String = "",
        val tripId: UUID? = null,
        val driverId: UUID? = null
    ) {
        init {
            require(sectionId.isNotEmpty() || tripId != null || driverId != null) {
                "At least one field must be not null or empty"
            }
        }

        companion object {

            fun from(domainCommandQuery: TripApplicationRepository.CommandQuery): TripApplicationCommandQuery {
                return TripApplicationCommandQuery(
                    sectionId = domainCommandQuery.sectionId,
                    tripId = domainCommandQuery.tripId,
                    driverId = domainCommandQuery.driverId
                )
            }
        }
    }

}