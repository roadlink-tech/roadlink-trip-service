package com.roadlink.tripservice.infrastructure.persistence.trip_application

import com.roadlink.tripservice.domain.trip_application.TripApplicationRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplication
import com.roadlink.tripservice.infrastructure.persistence.section.SectionJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
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
            find(TripApplicationRepository.CommandQuery(driverId = driverId)).toMutableList()
        }
    }

    override fun findByTripId(tripId: UUID): List<TripPlanApplication.TripApplication> {
        return transactionManager.executeRead {
            find(TripApplicationRepository.CommandQuery(tripId = tripId)).toMutableList()
        }
    }

    override fun find(commandQuery: TripApplicationRepository.CommandQuery): List<TripPlanApplication.TripApplication> {
        val cq = TripApplicationCommandQuery.from(commandQuery)
        return transactionManager.executeRead {
            val cb = entityManager.criteriaBuilder
            val criteriaQuery = cb.createQuery(TripApplicationJPAEntity::class.java)
            val root = criteriaQuery.from(TripApplicationJPAEntity::class.java)

            val predicates = mutableListOf<Predicate>()

            if (cq.sectionId.isNotEmpty()) {
                val sections = root.join<TripApplicationJPAEntity, SectionJPAEntity>("sections")
                val predicate = cb.equal(sections.get<String>("id"), cq.sectionId)
                predicates.add(predicate)
            }

            if (cq.tripId != null) {
                val sections = root.join<TripApplicationJPAEntity, SectionJPAEntity>("sections")
                val tripIdPredicate = cb.equal(sections.get<String>("tripId"), cq.tripId)
                predicates.add(tripIdPredicate)
            }

            if (cq.driverId != null) {
                val sections = root.join<TripApplicationJPAEntity, SectionJPAEntity>("sections")
                val driverIdPredicate = cb.equal(sections.get<String>("driverId"), cq.driverId.toString())
                predicates.add(driverIdPredicate)
            }
            criteriaQuery.select(root).where(cb.and(*predicates.toTypedArray()))

            entityManager.createQuery(criteriaQuery).resultList.map { it.toDomain() }
        }
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