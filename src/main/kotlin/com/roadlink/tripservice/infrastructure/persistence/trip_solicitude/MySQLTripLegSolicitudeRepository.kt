package com.roadlink.tripservice.infrastructure.persistence.trip_solicitude

import com.roadlink.tripservice.domain.trip_solicitude.TripLegSolicitudeRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitude
import com.roadlink.tripservice.infrastructure.persistence.section.SectionJPAEntity
import io.micronaut.transaction.TransactionOperations
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.hibernate.Session
import java.util.*

class MySQLTripLegSolicitudeRepository(
    private val entityManager: EntityManager,
    private val transactionManager: TransactionOperations<Session>,
) : TripLegSolicitudeRepository {
    override fun saveAll(tripLegSolicitudes: List<TripPlanSolicitude.TripLegSolicitude>) {
        // TODO batch write!
        for (tripApplication in tripLegSolicitudes) {
            entityManager.persist(TripLegSolicitudeJPAEntity.from(tripApplication))
        }
    }

    override fun find(commandQuery: TripLegSolicitudeRepository.CommandQuery): List<TripPlanSolicitude.TripLegSolicitude> {
        val cq = TripLegSolicitudeCommandQuery.from(commandQuery)
        return transactionManager.executeRead {
            val cb = entityManager.criteriaBuilder
            val criteriaQuery = cb.createQuery(TripLegSolicitudeJPAEntity::class.java)
            val root = criteriaQuery.from(TripLegSolicitudeJPAEntity::class.java)

            val predicates = mutableListOf<Predicate>()

            if (cq.sectionId.isNotEmpty()) {
                val sections = root.join<TripLegSolicitudeJPAEntity, SectionJPAEntity>("sections")
                val predicate = cb.equal(sections.get<String>("id"), cq.sectionId)
                predicates.add(predicate)
            }

            if (cq.tripId != null) {
                val sections = root.join<TripLegSolicitudeJPAEntity, SectionJPAEntity>("sections")
                val tripIdPredicate = cb.equal(sections.get<String>("tripId"), cq.tripId)
                predicates.add(tripIdPredicate)
            }

            if (cq.driverId != null) {
                val sections = root.join<TripLegSolicitudeJPAEntity, SectionJPAEntity>("sections")
                val driverIdPredicate =
                    cb.equal(sections.get<String>("driverId"), cq.driverId.toString())
                predicates.add(driverIdPredicate)
            }

            if (cq.status != null) {
                predicates.add(root.get<UUID>("status").`in`(cq.status.toString()))
            }

            criteriaQuery.select(root).where(cb.and(*predicates.toTypedArray()))

            entityManager.createQuery(criteriaQuery).resultList.map { it.toDomain() }
        }
    }

    data class TripLegSolicitudeCommandQuery(
        val sectionId: String = "",
        val tripId: UUID? = null,
        val driverId: UUID? = null,
        val status: TripPlanSolicitude.TripLegSolicitude.Status? = null
    ) {
        init {
            require(sectionId.isNotEmpty() || tripId != null || driverId != null || status == null) {
                "At least one field must be not null or empty"
            }
        }

        companion object {

            fun from(domainCommandQuery: TripLegSolicitudeRepository.CommandQuery): TripLegSolicitudeCommandQuery {
                return TripLegSolicitudeCommandQuery(
                    sectionId = domainCommandQuery.sectionId,
                    tripId = domainCommandQuery.tripId,
                    driverId = domainCommandQuery.driverId,
                    status = domainCommandQuery.status

                )
            }
        }
    }

}